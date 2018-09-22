import React, {Component} from 'react';
import InputMask from 'react-input-mask';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../constants'
import axios from "axios/index";
import './Registration.css';

class Registration extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            surname: '',
            patronymic: '',
            address: '',
            phone: '',
            login: '',
            password: '',
            confirm: '',
            errorMessage: null,
            errorMessages: {}
        }
    }

    queryRegistration() {
        const self = this;
        const p = self.state.phone.trim().split(' ');
        const validPhoneNumber = (p.length === 5) ? (p[0] + p[1] + p[2] + '-' + p[3] + '-' + p[4]) : ('');
        const url = getApiUrl('/auth/register');
        axios
            .post(url, {
                name: self.state.name,
                surname: self.state.surname,
                patronymic: self.state.patronymic,
                address: self.state.address,
                phone: validPhoneNumber,
                login: self.state.login,
                password: self.state.password
            })
            .then(function (response) {
                console.log('Registration Response:', response);
                window.location.replace('/login');
            })
            .catch(function (error) {
                const errorResponse = error.response;
                console.log('Registration Error Response:', errorResponse);
                const data = errorResponse.data;
                const message = data.message;
                const errors = data.errors;
                if (errors != null) {
                    self.setState({
                        errorMessage: message,
                        errorMessages: errors
                    });
                } else {
                    self.setState({
                        errorMessage: message
                    });
                }
            })
    }

    handleSubmit(event) {
        event.preventDefault();
        this.setState({
            errorMessage: null,
            errorMessages: {}
        });
        if (this.state.password === this.state.confirm) {
            this.queryRegistration();
        } else {
            this.setState({
                errorMessages: {password: 'Введенные пароли не совпадают!'}
            });
        }
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({
            [target.id]: target.value
        });
    }

    render() {
        return (
            <div className="Registration">
                <div className="container">
                    <form onSubmit={this.handleSubmit.bind(this)}>
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
                </div>
            </div>
        );
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
                <InputMask type="text" id={props.identifier} placeholder={props.placeholder}
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