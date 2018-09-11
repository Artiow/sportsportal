import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './Login.css';
import axios from "axios/index";

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            loginValue: '',
            passwordValue: '',
            errorMessage: ''
        };
    }

    query() {
        const self = this;
        axios
            .get('http://localhost:8080/auth/login', {
                params: {
                    login: self.state.loginValue,
                    password: self.state.passwordValue
                }
            })
            .then(function (response) {
                console.log('Response:', response);
                const data = response.data;
                localStorage.setItem('token', (data.tokenType + ' ' + data.tokenHash));
                localStorage.setItem('user', data.userInfo);
                window.location.replace('/');
            })
            .catch(function (error) {
                const response = error.response;
                console.log('Error Response:', response);
                self.setState({
                    errorMessage: response.data.message
                });
            })
    }

    handleSubmit(event) {
        event.preventDefault();
        this.query();
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({
            [target.name]: target.value
        });
    }

    render() {
        return (
            <div className="Login">
                <div className="panel panel-top">
                    <h2>Авторизация</h2>
                    <p>Введите логин и пароль</p>
                </div>
                <form onSubmit={this.handleSubmit.bind(this)}>
                    <div>
                        <input type="text" className="form-control" name="loginValue" placeholder="Логин"
                               required="required" value={this.state.loginValue}
                               onChange={this.handleInputChange.bind(this)}/>
                        <input type="password" className="form-control" name="passwordValue" placeholder="Пароль"
                               required="required" value={this.state.passwordValue}
                               onChange={this.handleInputChange.bind(this)}/>
                    </div>
                    <div>
                        <div className="forgot"><Link to="/registration">Нет аккаунта?</Link></div>
                        <button type="submit" className="btn btn-primary btn-lg btn-block">
                            Авторизация
                        </button>
                    </div>
                </form>
                <div className="panel panel-bottom">
                    <p>{this.state.errorMessage}</p>
                </div>
            </div>
        );
    }
}

export default Login;