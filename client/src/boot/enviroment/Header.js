import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../constants'
import axios from "axios/index";
import './Header.css';

function Header(props) {
    return (
        <header className="Header">
            <div className="container">
                <div className="row">
                    <MainBlock titleHref={props.titleHref} titleLabel={props.titleLabel}
                               subtitleHref={props.subtitleHref} subtitleLabel={props.subtitleLabel}/>
                    <AuthBlock/>
                </div>
            </div>
        </header>
    );
}

function MainBlock(props) {
    return (
        <div className="MainBlock col-md-6">
            {((props.titleHref != null) && (props.titleLabel != null)) ? (
                <h3><Link to={props.titleHref}>{props.titleLabel}</Link></h3>
            ) : (null)}
            {((props.subtitleHref != null) && (props.subtitleLabel != null)) ? (
                <h6><Link to={props.subtitleHref}>
                    <i className="fa fa-angle-double-left"/>
                    <small>{props.subtitleLabel}</small>
                </Link></h6>
            ) : (null)}
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
        this.queryLogout();
        window.location.replace('/login');
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
                .get(getApiUrl('/auth/verify'), {params: {accessToken: accessToken}})
                .then(function (response) {
                    console.log('Verify Response:', response);
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
                .catch(function (error) {
                    console.log('Verify Error:', ((error.response != null) ? error.response : error));
                    localStorage.clear();
                })
        }
    }

    render() {
        return (
            <div className="AuthBlock col-md-6">
                {this.state.isAuthorized ? (
                    <div className="auth">
                        <Link to="/home"><i className="fa fa-user"/>
                            <span>{this.state.nickname}</span>
                        </Link>
                        <Link to="/logout" onClick={this.handleLogout.bind(this)}>
                            <i className="fa fa-sign-out"/>
                            <span>ВЫЙТИ</span>
                        </Link>
                    </div>
                ) : (
                    <div className="auth">
                        <Link to="/login"><i className="fa fa-sign-in"/>ВОЙТИ</Link>
                    </div>
                )}
            </div>
        );
    }
}

export default Header;