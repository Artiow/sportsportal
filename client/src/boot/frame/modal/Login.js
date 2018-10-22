import React from 'react';
import {Link} from 'react-router-dom';
import apiUrl, {env} from '../../constants';
import axios from 'axios';
import './Login.css';

export default class Login extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';

    constructor(props) {
        super(props);
        this.state = {
            email: null,
            password: null,
            errorMessage: null,
            loading: false
        };
    }

    reset() {
        this.submitForm.reset();
        this.setState({errorMessage: null});
    }

    queryLogin() {
        this.setState({loading: true});
        axios
            .get(apiUrl('/auth/login'), {
                params: {
                    email: this.state.email,
                    password: this.state.password
                }
            })
            .then(response => {
                console.debug('Login (query):', response);
                const onSuccess = this.props.onSuccess;
                if (typeof onSuccess === 'function') onSuccess(response.data);
                setTimeout(() => this.setState({loading: false}), env.ANIMATION_TIMEOUT);
            })
            .catch(error => {
                let errorMessage;
                const errorResponse = error.response;
                console.warn('Login (query):', errorResponse ? errorResponse : error);
                errorMessage = errorResponse ? errorResponse.data.message : Login.UNEXPECTED_ERROR_MESSAGE;
                this.setState({errorMessage: errorMessage, loading: false});
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
                <form onSubmit={this.handleSubmit.bind(this)}
                      ref={form => this.submitForm = form}>
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
                        <button className="btn btn-primary btn-lg btn-block"
                                disabled={this.state.loading} type="submit">
                            {(!this.state.loading) ? (
                                <span>Авторизация</span>
                            ) : (
                                <i className="fa fa-refresh fa-spin fa-fw"/>
                            )}
                        </button>
                    </div>
                </form>
            </div>
        );
    }
}