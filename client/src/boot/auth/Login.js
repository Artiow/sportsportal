import React, {Component} from 'react';
import './Login.css';

class Login extends Component {
    render() {
        const errorMessage = '';
        return (
            <div className="Login">
                <div className="panel panel-top">
                    <h2>Авторизация</h2>
                    <p>Введите логин и пароль</p>
                </div>
                <form>
                    <div>
                        <input type="text" className="form-control" id="login" placeholder="Логин"
                               required="required"/>
                        <input type="password" className="form-control" id="password" placeholder="Пароль"
                               required="required"/>
                    </div>
                    <div>
                        <div className="forgot"><a href="/registration">Нет аккаунта?</a></div>
                        <button type="submit" className="btn btn-primary btn-lg btn-block">Авторизация</button>
                    </div>
                </form>
                <div className="panel panel-bottom">
                    <p>{errorMessage}</p>
                </div>
            </div>
        );
    }
}

export default Login;