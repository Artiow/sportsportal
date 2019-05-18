import React from 'react';
import Authentication from '../../../connector/Authentication';
import {Link} from 'react-router-dom';
import './Login.css';

export default class Login extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';

    constructor(props) {
        super(props);
        this.state = {
            email: null,
            password: null,
            errorMessage: null,
            inProcess: false
        };
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            errorMessage: null,
            inProcess: false
        });
    }

    queryLogin() {
        this.setState({
            errorMessage: null,
            inProcess: true
        });
        Authentication.login(
            this.state.email,
            this.state.password
        ).then(response => {
            console.debug('Login', 'query', 'success');
            const onSuccess = this.props.onSuccess;
            if (typeof onSuccess === 'function') onSuccess();
        }).catch(error => {
            (error ? console.warn : console.error)('Login', 'query', 'failed');
            this.setState({
                errorMessage: error ? error.message : Login.UNEXPECTED_ERROR_MESSAGE,
                inProcess: false
            });
        });
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
        const onRegClick = !this.state.inProcess ? this.props.onRegClick : null;
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
                                disabled={this.state.inProcess} type="submit">
                            {(!this.state.inProcess) ? (
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