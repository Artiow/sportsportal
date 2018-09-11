import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import './Header.css';
import axios from "axios/index";

function Header(props) {
    return (
        <header className="Header">
            <div className="container">
                <div className="row">
                    <MainBlock titleHref={'/'} titleLabel={'АРЕНДА ПЛОЩАДОК'}/>
                    <AuthBlock/>
                </div>
            </div>
        </header>
    );
}

function MainBlock(props) {
    return (
        <div className="col-md-3">
            <h3>
                <Link to={props.titleHref}>{props.titleLabel}</Link>
            </h3>
            <h6>
                <Link to="/">
                    <i className="fa fa-angle-double-left"/>
                    <small>НА ГЛАВНУЮ</small>
                </Link>
            </h6>
        </div>
    );
}

class AuthBlock extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthorized: false,
            nickname: null
        };

        this.queryVerify();
    }

    handleLogout(event) {
        event.preventDefault();
        this.queryLogout()
    }

    queryLogout() {
        localStorage.clear();
        this.setState({
            isAuthorized: false,
            nickname: null
        });
    }

    queryVerify() {
        const self = this;
        const accessToken = localStorage.getItem('token');
        if (accessToken !== null) {
            axios
                .get('http://localhost:8080/auth/verify', {params: {accessToken: accessToken}})
                .then(function (response) {
                    console.log('Response:', response);
                    const data = response.data;
                    const userInfo = data.userInfo;
                    const login = userInfo.login;
                    localStorage.setItem('token', (data.tokenType + ' ' + data.tokenHash));
                    localStorage.setItem('user', userInfo);
                    const nickname = login.charAt(0).toUpperCase() + login.slice(1);
                    self.setState({
                        isAuthorized: true,
                        nickname: nickname
                    })
                })
        }
    }

    authContent() {
        if (this.state.isAuthorized) {
            return (
                <div className="auth">
                    <Link to="/home"><i className="fa fa-user"/>
                        <span>{this.state.nickname}</span>
                    </Link>
                    <Link to="/logout" onClick={this.handleLogout.bind(this)}>
                        <i className="fa fa-sign-out"/>
                        <span>ВЫЙТИ</span>
                    </Link>
                </div>
            );
        } else {
            return (
                <div className="auth">
                    <Link to="/login"><i className="fa fa-sign-in"/>ВОЙТИ</Link>
                </div>
            );
        }
    }

    render() {
        return (
            <div className="AuthBlock offset-md-6 col-md-3">
                {this.authContent()}
            </div>
        );
    }
}

export default Header;