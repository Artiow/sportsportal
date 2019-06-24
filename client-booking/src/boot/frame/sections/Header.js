import React from 'react';
import {Link} from 'react-router-dom';
import {env} from '../../constants'
import logo from '../logo.png';
import './Header.css';

export default function Header(props) {
    return (
        <header className="Header">
            <div className="container">
                <div className="row">
                    <MainBlock breadcrumb={props.breadcrumb}/>
                    <AuthBlock onLogin={props.onLogin} onLogout={props.onLogout}
                               username={props.username}/>
                </div>
            </div>
        </header>
    );
}

function MainBlock(props) {

    const TITLE = 'Бронирование площадок';
    const LOGO_W = 75;
    const LOGO_H = 75;

    let breadcrumb = props.breadcrumb;
    const initBreadcrumb = [{link: '/', title: 'Главная'}];
    if (breadcrumb != null) breadcrumb = initBreadcrumb.concat(breadcrumb);
    return (
        <div className="MainBlock col-8">
            <div className="sub-block">
                <a href={env.MAIN_HOST}>
                    <img src={logo} width={LOGO_W} height={LOGO_H} alt="logo"/>
                </a>
            </div>
            <div className="sub-block">
                <Link to="/"><h3>{TITLE}</h3></Link>
                <MainBreadcrumb breadcrumb={breadcrumb}/>
            </div>
        </div>
    );
}

function MainBreadcrumb(props) {
    const element = (link, title, key, active) => {
        return active ? (
            <li key={key} className="breadcrumb-item active">{title}</li>
        ) : (
            <li key={key} className="breadcrumb-item"><Link to={link}>{title}</Link></li>
        );
    };
    const elements = [];
    const breadcrumb = props.breadcrumb;
    if (breadcrumb != null) {
        const lastIndex = breadcrumb.length - 1;
        breadcrumb.forEach((value, index) => {
            elements.push(element(value.link, value.title, index, (index === lastIndex)))
        })
    }
    return (<ol className="breadcrumb">{elements}</ol>);
}

class AuthBlock extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthorized: !!props.username,
            username: props.username ? props.username : null
        };
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.username !== this.props.username) {
            this.setState({
                isAuthorized: !!nextProps.username,
                username: nextProps.username ? nextProps.username : null
            })
        }
    }

    handleLogin(event) {
        event.preventDefault();
        const onLogin = this.props.onLogin;
        if (typeof onLogin === 'function') onLogin(event);
    }

    handleLogout(event) {
        event.preventDefault();
        const onLogout = this.props.onLogout;
        if (typeof onLogout === 'function') onLogout(event);
    }

    render() {
        return (
            <div className="AuthBlock col-4">
                {this.state.isAuthorized ? (
                    <div className="auth">
                        <Link to="/home" className="row">
                            <i className="fa fa-user col-1"/>
                            <span className="col-11 col-label">{this.state.username}</span>
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