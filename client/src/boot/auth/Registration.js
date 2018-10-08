import React, {Component} from 'react';
import InputMask from 'react-input-mask';
import {Link} from 'react-router-dom';
import apiUrl from '../constants'
import axios from 'axios';
import qs from 'qs';
import './Registration.css';

class Registration extends Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';
    static STAGE = Object.freeze({REGISTRATION: 1, CONFIRMATION: 2});

    constructor(props) {
        super(props);
        this.state = {
            stage: Registration.STAGE.REGISTRATION,
            confirmId: null,
            confirmEmail: null,
            errorMessage: null,
            errorMessages: {}
        }
    }

    querySendConfirmMessage() {
        axios
            .put(apiUrl('/auth/confirm/' + this.state.confirmId), '', {
                params: {host: window.location.origin},
                paramsSerializer: (params => qs.stringify(params))
            })
            .then(function (response) {
                console.log('Confirm Message Response:', response);
            })
            .catch(function (error) {
                console.log('Confirm Message Error:', ((error.response != null) ? error.response : error));
            })
    }

    queryRegistration(obj) {
        const self = this;
        this.setState({errorMessage: null, errorMessages: {}});
        axios
            .post(apiUrl('/auth/register'), obj)
            .then(function (response) {
                console.log('Registration Response:', response);
                const locationArray = response.headers.location.split('/');
                self.setState({
                    stage: Registration.STAGE.CONFIRMATION,
                    confirmId: locationArray[locationArray.length - 1],
                    confirmEmail: obj.email,
                })
            })
            .catch(function (error) {
                const errorResponse = error.response;
                console.log('Registration Error:', ((errorResponse != null) ? errorResponse : error));
                if (errorResponse != null) {
                    const data = errorResponse.data;
                    const message = data.message;
                    const errors = data.errors;
                    if (errors != null) self.setState({errorMessage: message, errorMessages: errors});
                    else self.setState({errorMessage: message});
                } else self.setState({errorMessage: Registration.UNEXPECTED_ERROR_MESSAGE});
            })
    }

    render() {
        const contentByStage = (stage) => {
            switch (stage) {
                case Registration.STAGE.REGISTRATION:
                    return (
                        <RegistrationForm errorMessage={this.state.errorMessage}
                                          errorMessages={this.state.errorMessages}
                                          onSubmit={this.queryRegistration.bind(this)}/>
                    );
                case Registration.STAGE.CONFIRMATION:
                    this.querySendConfirmMessage();
                    return (
                        <div className="ConfirmationMessage alert">
                            <h4 className="alert-heading">Подтвердите вашу учетную запись!</h4>
                            <p>Мы отправили на указанный вами почтовый
                                ящик <code>{this.state.confirmEmail}</code> письмо с ссылкой для подтверждения вашей
                                учетной записи.</p>
                            <hr/>
                            <p>Пожалуйста, перейдите по ссылке, указанной в нем.</p>
                        </div>
                    );
                default:
                    return (null);
            }
        };
        return (
            <div className="Registration">
                <div className="container">{contentByStage(this.state.stage)}</div>
            </div>
        );
    }
}

class RegistrationForm extends Component {

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
        this.setState({[target.id]: target.value});
    }

    render() {
        return (
            <form className="RegistrationForm" onSubmit={this.handleSubmit.bind(this)}>
                <h1>Регистрация</h1>
                <div style={(this.state.errorMessage == null) ? {display: 'none'} : {display: 'block'}}
                     className="alert alert-warning">
                    {this.state.errorMessage}
                </div>
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
                <PasswordInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                    secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}
                                    errorMessage={this.state.errorMessages.password}
                                    onChange={this.handleInputChange.bind(this)}
                                    required={'required'}/>
                <div className="row">
                    <div className="col-sm-6 offset-sm-3">
                        <button type="submit" className="btn btn-primary btn-lg btn-block">Регистрация</button>
                        <div className="login-link-box">
                            <Link className="btn btn-link btn-sm" to="/login">Авторизация</Link>
                            <Link className="btn btn-link btn-sm" to="/">На главную</Link>
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
                <InputMask type={(props.type != null) ? props.type : 'text'}
                           id={props.identifier} placeholder={props.placeholder}
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
                    <input type="password" id={props.firstIdentifier} placeholder={props.firstPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                </div>
            </div>
            <div className="form-row">
                <label htmlFor={props.secondIdentifier} className="col-sm-3 col-form-label"/>
                <div className="col-sm-9">
                    <input type="password" id={props.secondIdentifier} placeholder={props.secondPlaceholder}
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

export default Registration;