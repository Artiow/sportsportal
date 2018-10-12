import React from 'react';
import InputMask from 'react-input-mask';
import {Link} from 'react-router-dom';
import apiUrl, {ID} from '../../constants';
import axios from 'axios';
import './Registration.css';

export default class Registration extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';

    constructor(props) {
        super(props);
        this.state = {
            errorMessage: null,
            errorMessages: {}
        }
    }

    queryRegistration(obj) {
        const self = this;
        this.setState({errorMessage: null, errorMessages: {}});
        axios
            .post(apiUrl('/auth/register'), obj)
            .then(function (response) {
                console.log('Registration (query):', response);
                const locationArray = response.headers.location.split('/');
                const onSuccess = self.props.onSuccess;
                if (typeof onSuccess === 'function') onSuccess(locationArray[locationArray.length - 1], obj.email);
            })
            .catch(function (error) {
                const errorResponse = error.response;
                if (errorResponse != null) {
                    const data = errorResponse.data;
                    const message = data.message;
                    const errors = data.errors;
                    console.warn('Registration (query):', errorResponse);
                    if (errors != null) self.setState({errorMessage: message, errorMessages: errors});
                    else self.setState({errorMessage: message});
                } else {
                    console.error('Registration (query):', error);
                    self.setState({errorMessage: Registration.UNEXPECTED_ERROR_MESSAGE});
                }
            })
    }

    render() {
        return (
            <div className="Registration">
                <RegistrationForm errorMessage={this.state.errorMessage}
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
            name: '',
            surname: '',
            email: '',
            password: '',
            confirm: '',
            errorMessage: ((props.errorMessage != null) ? props.errorMessage : null),
            errorMessages: ((props.errorMessages != null) ? props.errorMessages : {})
        }
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
            <form className="RegistrationForm" onSubmit={this.handleSubmit.bind(this)}>
                <div style={(this.state.errorMessage == null) ? {display: 'none'} : {display: 'block'}}
                     className="alert alert-warning">
                    {this.state.errorMessage}
                </div>
                <div className="form-row-main-container">
                    <div className="form-row-container">
                        <InputField identifier={'name'} placeholder={'Имя'} required={'required'}
                                    errorMessage={this.state.errorMessages.name}
                                    onChange={this.handleInputChange.bind(this)}/>
                        <InputField identifier={'surname'} placeholder={'Фамилия'} required={'required'}
                                    errorMessage={this.state.errorMessages.surname}
                                    onChange={this.handleInputChange.bind(this)}/>
                        <InputField type={'email'}
                                    identifier={'email'} placeholder={'Email'} required={'required'}
                                    errorMessage={this.state.errorMessages.email}
                                    onChange={this.handleInputChange.bind(this)}/>
                    </div>
                    <PasswordInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                        secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}
                                        errorMessage={this.state.errorMessages.password}
                                        onChange={this.handleInputChange.bind(this)}
                                        required={'required'}/>
                </div>
                <div className="row">
                    <div className="col-sm-6 offset-sm-3">
                        <button type="submit" className="btn btn-primary btn-lg btn-block">Регистрация</button>
                        <div className="login">
                            <Link to="/registration"
                                  onClick={this.handleLogClick.bind(this)}>
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
                <InputMask id={ID() + props.identifier}
                           type={(props.type != null) ? props.type : 'text'}
                           name={props.identifier} placeholder={props.placeholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           maskChar={props.maskChar} mask={props.mask}
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
                    <input id={ID() + props.firstIdentifier} type="password"
                           name={props.firstIdentifier} placeholder={props.firstPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                </div>
            </div>
            <div className="form-row">
                <label htmlFor={props.secondIdentifier} className="col-sm-3 col-form-label"/>
                <div className="col-sm-9">
                    <input id={ID() + props.secondIdentifier} type="password"
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