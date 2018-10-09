import React from 'react';
import {Link} from 'react-router-dom';
import verify, {getLogin, logout} from '../../../util/verification';
import './Header.css';

export default function Header(props) {
    return (
        <header className="Header">
            <div className="container">
                <div className="row">
                    <MainBlock {...props}/>
                    <AuthBlock onLogin={props.onLogin}
                               onLogout={props.onLogout}/>
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

class AuthBlock extends React.Component {
    constructor(props) {
        super(props);
        const login = getLogin();
        const logged = (login != null);
        this.state = {
            isAuthorized: logged,
            nickname: logged ? login.username : null
        };
        this.queryVerify();
    }

    handleLogin(event) {
        event.preventDefault();
        const onLogin = this.props.onLogin;
        if (typeof onLogin === 'function') onLogin(event);
    }

    handleLogout(event) {
        event.preventDefault();
        this.queryLogout();
        const onLogout = this.props.onLogout;
        if (typeof onLogout === 'function') onLogout(event);
    }

    queryLogout() {
        logout();
        this.setState({
            isAuthorized: false,
            nickname: null
        });
    }

    queryVerify() {
        const self = this;
        verify(function (response) {
            self.setState({
                isAuthorized: true,
                nickname: response.data.login.username
            })
        }, function (error) {
            self.setState({
                isAuthorized: false,
                nickname: null
            });
        });
    }

    render() {
        return (
            <div className="AuthBlock col-md-6">
                {this.state.isAuthorized ? (
                    <div className="auth">
                        <Link to="/home"
                              className="row">
                            <i className="fa fa-user col-1"/>
                            <span className="col-11 col-label">{this.state.nickname}</span>
                        </Link>
                        <Link to="/logout" className="row"
                              onClick={this.handleLogout.bind(this)}>
                            <i className="fa fa-sign-out col-1"/>
                            <span className="col-11 col-label">Выйти</span>
                        </Link>
                    </div>
                ) : (
                    <div className="auth">
                        <Link to="/login" className="row"
                              onClick={this.handleLogin.bind(this)}>
                            <i className="fa fa-sign-in col-1"/>
                            <span className="col-11 col-label">Войти</span>
                        </Link>
                    </div>
                )}
            </div>
        );
    }
}