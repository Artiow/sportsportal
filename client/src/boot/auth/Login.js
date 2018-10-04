import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../constants'
import verify from '../../util/verification'
import axios from 'axios';
import './Login.css';

class Login extends Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';

    constructor(props) {
        super(props);
        this.state = {
            login: '',
            password: '',
            errorMessage: ''
        };
        verify(function () {
            window.location.replace('/');
        });
    }

    queryLogin() {
        const self = this;
        const url = getApiUrl('/auth/login');
        axios
            .get(url, {params: {login: self.state.login, password: self.state.password}})
            .then(function (response) {
                console.log('Login Response:', response);
                const data = response.data;
                localStorage.setItem('token', (data.tokenType + ' ' + data.tokenHash));
                localStorage.setItem('user', data.userInfo);
                window.location.replace('/');
            })
            .catch(function (error) {
                const errorResponse = error.response;
                console.log('Login Error:', ((errorResponse != null) ? errorResponse : error));
                self.setState({errorMessage: (errorResponse != null) ? errorResponse.data.message : Login.UNEXPECTED_ERROR_MESSAGE});
            })
    }

    handleSubmit(event) {
        event.preventDefault();
        this.queryLogin();
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({
            [target.id]: target.value
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
                        <input type="text" className="form-control" id="login" placeholder="Логин"
                               onChange={this.handleInputChange.bind(this)}
                               required="required"/>
                        <input type="password" className="form-control" id="password" placeholder="Пароль"
                               onChange={this.handleInputChange.bind(this)}
                               required="required"/>
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