import React, {Component} from 'react';
import InputMask from 'react-input-mask';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../constants'
import axios from "axios/index";
import './Registration.css';

class Registration extends Component {

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
        axios
            .post(getApiUrl('/auth/register'), {
                name: obj.name,
                surname: obj.surname,
                patronymic: obj.patronymic,
                address: obj.address,
                phone: obj.phone,
                login: obj.login,
                email: obj.email,
                password: obj.password
            })
            .then(function (response) {
                console.log('Registration Response:', response);
                window.location.replace('/login');
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

    handleInputChange(event) {
        const target = event.target;
        this.setState({[target.id]: target.value});
    }

    render() {
        return (
            <div className="Registration">
                <div className="container">
                    <RegistrationForm errorMessage={this.state.errorMessage} errorMessages={this.state.errorMessages}
                                      onSubmit={this.queryRegistration.bind(this)}/>
                </div>
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
            patronymic: '',
            address: '',
            phone: '',
            login: '',
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
            const pDigits = this.state.phone.trim().split(' ');
            if (typeof onSubmit === 'function') onSubmit({
                name: this.state.name,
                surname: this.state.surname,
                patronymic: this.state.patronymic,
                address: this.state.address,
                phone: ((pDigits.length === 5) ? (pDigits[0] + pDigits[1] + pDigits[2] + '-' + pDigits[3] + '-' + pDigits[4]) : (pDigits.join(' '))),
                login: this.state.login,
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
                <InputField identifier={'name'} placeholder={'Имя'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.name}/>
                <InputField identifier={'surname'} placeholder={'Фамилия'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.surname}/>
                <InputField identifier={'patronymic'} placeholder={'Отчество'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.patronymic}/>
                <InputField identifier={'address'} placeholder={'Адрес'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.address}/>
                <InputField identifier={'phone'} placeholder={'Телефон'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.phone}
                            maskChar=" " mask="+7 (999) 999 99 99"/>
                <InputField identifier={'login'} placeholder={'Логин'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.login}/>
                <InputField type={'email'}
                            identifier={'email'} placeholder={'Эл. Почта'}
                            onChange={this.handleInputChange.bind(this)}
                            errorMessage={this.state.errorMessages.email}/>
                <PasswordInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                    secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}
                                    onChange={this.handleInputChange.bind(this)}
                                    errorMessage={this.state.errorMessages.password}/>
                <div className="row">
                    <div className="col-sm-6 offset-sm-3">
                        <button type="submit" className="btn btn-primary btn-lg btn-block">Регистрация</button>
                        <div className="login-link-box">
                            <Link className="btn btn-link btn-sm" to="/login">Авторизация</Link>
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
                           required="required"/>
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
                           required="required"/>
                </div>
            </div>
            <div className="form-row">
                <label htmlFor={props.secondIdentifier} className="col-sm-3 col-form-label"/>
                <div className="col-sm-9">
                    <input type="password" id={props.secondIdentifier} placeholder={props.secondPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required="required"/>
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