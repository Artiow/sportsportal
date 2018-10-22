import React from 'react';
import InputMask from 'react-input-mask';
import {Link} from 'react-router-dom';
import apiUrl from '../../constants';
import axios from 'axios';
import './Registration.css';

export default class Registration extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';

    constructor(props) {
        super(props);
        this.state = {
            errorMessage: null,
            errorMessages: {},
            loading: false
        }
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            errorMessage: null,
            errorMessages: {},
            loading: false
        });
    }

    queryRegistration(obj) {
        this.setState({
            errorMessage: null,
            errorMessages: {},
            loading: true
        });
        axios
            .post(apiUrl('/auth/register'), obj)
            .then(response => {
                console.debug('Registration (query):', response);
                const locationArray = response.headers.location.split('/');
                const onSuccess = this.props.onSuccess;
                if (typeof onSuccess === 'function') onSuccess(locationArray[locationArray.length - 1], obj.email);
            })
            .catch(error => {
                const errorResponse = error.response;
                if (errorResponse != null) {
                    const data = errorResponse.data;
                    const message = data.message;
                    const errors = data.errors;
                    console.warn('Registration (query):', errorResponse);
                    this.setState({
                        errorMessage: message,
                        errorMessages: errors,
                        loading: false
                    });
                } else {
                    console.error('Registration (query):', error);
                    this.setState({
                        errorMessage: Registration.UNEXPECTED_ERROR_MESSAGE,
                        loading: false
                    });
                }
            })
    }

    render() {
        return (
            <div className="Registration">
                <RegistrationForm inProcess={this.state.loading}
                                  ref={form => this.submitForm = form}
                                  errorMessage={this.state.errorMessage}
                                  errorMessages={this.state.errorMessages}
                                  onSubmit={this.queryRegistration.bind(this)}
                                  onLogClick={this.props.onLogClick}/>
            </div>
        );
    }
}

class RegistrationForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: null,
            surname: null,
            email: null,
            password: null,
            confirm: null,
            errorMessage: ((props.errorMessage != null) ? props.errorMessage : null),
            errorMessages: ((props.errorMessages != null) ? props.errorMessages : {})
        }
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            errorMessage: ((this.props.errorMessage != null) ? this.props.errorMessage : null),
            errorMessages: ((this.props.errorMessages != null) ? this.props.errorMessages : {})
        });
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            errorMessage: ((nextProps.errorMessage != null) ? nextProps.errorMessage : null),
            errorMessages: ((nextProps.errorMessages != null) ? nextProps.errorMessages : {})
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.setState({errorMessage: null, errorMessages: {}});
        if (this.state.password === this.state.confirm) {
            const onSubmit = this.props.onSubmit;
            if (typeof onSubmit === 'function') onSubmit({
                name: this.state.name,
                surname: this.state.surname,
                email: this.state.email,
                password: this.state.password
            });
        }
        else {
            this.setState({errorMessages: {password: 'Введенные пароли не совпадают!'}});
        }
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({[target.name]: target.value});
    }

    handleLogClick(event) {
        event.preventDefault();
        const onLogClick = this.props.onLogClick;
        if (typeof onLogClick === 'function') onLogClick(event);
    }

    render() {
        return (
            <form className="RegistrationForm"
                  onSubmit={this.handleSubmit.bind(this)}
                  ref={form => this.submitForm = form}>
                <div style={(this.state.errorMessage == null) ? {display: 'none'} : {display: 'block'}}
                     className="alert alert-warning">
                    {this.state.errorMessage}
                </div>
                <div className="form-row-main-container">
                    <div className="form-row-container">
                        <InputField identifier={'name'} placeholder={'Имя'}
                                    errorMessage={this.state.errorMessages.name}
                                    onChange={this.handleInputChange.bind(this)}
                                    required={'required'}/>
                        <InputField identifier={'surname'} placeholder={'Фамилия'}
                                    errorMessage={this.state.errorMessages.surname}
                                    onChange={this.handleInputChange.bind(this)}
                                    required={'required'}/>
                        <InputField identifier={'email'} placeholder={'Email'}
                                    type={'email'} autoComplete="email"
                                    errorMessage={this.state.errorMessages.email}
                                    onChange={this.handleInputChange.bind(this)}
                                    required={'required'}/>
                    </div>
                    <PasswordInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                        secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}
                                        errorMessage={this.state.errorMessages.password}
                                        onChange={this.handleInputChange.bind(this)}
                                        required={'required'}/>
                </div>
                <div className="row">
                    <div className="col-sm-6 offset-sm-3">
                        <button className="btn btn-primary btn-lg btn-block"
                                disabled={this.props.inProcess} type="submit">
                            {(!this.props.inProcess) ? (
                                <span>Регистрация</span>
                            ) : (
                                <i className="fa fa-refresh fa-spin fa-fw"/>
                            )}
                        </button>
                        <div className="login">
                            <Link onClick={this.handleLogClick.bind(this)}
                                  to="/registration">
                                Авторизация
                            </Link>
                        </div>
                    </div>
                </div>
            </form>
        )
    }
}

function InputField(props) {
    const error = ((props.errorMessage != null) && (props.errorMessage !== ''));
    return (
        <div className="form-row">
            <label htmlFor={props.identifier} className="col-sm-3 col-form-label">
                {props.placeholder}
            </label>
            <div className="col-sm-9">
                <InputMask id={`reg_form_${props.identifier}`}
                           type={(props.type != null) ? props.type : 'text'}
                           name={props.identifier} placeholder={props.placeholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           maskChar={props.maskChar} mask={props.mask}
                           autoComplete={props.autoComplete}
                           onChange={props.onChange}
                           required={props.required}/>
                <div style={(!error) ? {display: 'none'} : {display: 'block'}}
                     className="invalid-feedback">
                    {props.errorMessage}
                </div>
            </div>
        </div>
    );
}

function PasswordInputField(props) {
    const error = ((props.errorMessage != null) && (props.errorMessage !== ''));
    return (
        <div className="form-row-container">
            <div className="form-row">
                <label htmlFor={props.firstIdentifier} className="col-sm-3 col-form-label">
                    {props.firstPlaceholder}
                </label>
                <div className="col-sm-9">
                    <input id={`reg_form_${props.firstIdentifier}`}
                           type="password" autoComplete="new-password"
                           name={props.firstIdentifier} placeholder={props.firstPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                </div>
            </div>
            <div className="form-row">
                <label htmlFor={props.secondIdentifier} className="col-sm-3 col-form-label"/>
                <div className="col-sm-9">
                    <input id={`reg_form_${props.secondIdentifier}`}
                           type="password" autoComplete="new-password"
                           name={props.secondIdentifier} placeholder={props.secondPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                    <div style={(!error) ? {display: 'none'} : {display: 'block'}}
                         className="invalid-feedback">
                        {props.errorMessage}
                    </div>
                </div>
            </div>
        </div>
    );
}