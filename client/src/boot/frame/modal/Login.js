import React from 'react';
import {Link} from 'react-router-dom';
import apiUrl from '../../constants';
import {login} from '../../../util/verification';
import axios from 'axios';
import './Login.css';

export default class Login extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';

    constructor(props) {
        super(props);
        this.state = {
            email: null,
            password: null,
            errorMessage: null
        };
    }

    queryLogin() {
        const self = this;
        axios
            .get(apiUrl('/auth/login'), {
                params: {
                    email: this.state.email,
                    password: this.state.password
                }
            })
            .then(function (response) {
                login(response.data);
                console.debug('Login (query):', response);
                window.location.replace('/');
            })
            .catch(function (error) {
                let errorMessage;
                const errorResponse = error.response;
                if (errorResponse != null) {
                    console.warn('Login (query):', errorResponse);
                    errorMessage = errorResponse.data.message;
                } else {
                    console.error('Login (query):', error);
                    errorMessage = Login.UNEXPECTED_ERROR_MESSAGE;
                }
                self.setState({errorMessage: errorMessage});
            })
    }

    handleSubmit(event) {
        event.preventDefault();
        this.queryLogin();
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({[target.name]: target.value});
    }

    handleRegClick(event) {
        event.preventDefault();
        const onRegClick = this.props.onRegClick;
        if (typeof onRegClick === 'function') onRegClick(event);
    }

    render() {
        return (
            <div className="Login">
                <div className="panel panel-top">
                    <p className={this.state.errorMessage ? 'error-message' : null}>
                        {this.state.errorMessage ? this.state.errorMessage : 'Введите свой адрес почты и пароль'}
                    </p>
                </div>
                <form onSubmit={this.handleSubmit.bind(this)}>
                    <div>
                        <input id="log_form_email" name="email"
                               type="email" autoComplete="email"
                               className="form-control" placeholder="Электронная почта"
                               onChange={this.handleInputChange.bind(this)}
                               required="required"/>
                        <input id="log_form_password" name="password"
                               type="password" autoComplete="password"
                               className="form-control" placeholder="Пароль"
                               onChange={this.handleInputChange.bind(this)}
                               required="required"/>
                    </div>
                    <div>
                        <div className="forgot">
                            <Link to="/registration"
                                  onClick={this.handleRegClick.bind(this)}>
                                Нет аккаунта?
                            </Link>
                        </div>
                        <button type="submit" className="btn btn-primary btn-lg btn-block">Авторизация</button>
                    </div>
                </form>
            </div>
        );
    }
}