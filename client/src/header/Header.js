import React, {Component} from 'react';
import './Header.css';

class Header extends Component {
    render() {
        return (
            <header className="Header">
                <div className="container">
                    <div className="row">
                        <MainBlock titleLabel={'TITLE'}/>
                        <AuthBlock/>
                    </div>
                </div>
            </header>
        );
    }
}

class MainBlock extends Component {
    render() {
        return (
            <div className="col-md-3">
                <h3>
                    <a href="#">{this.props.titleLabel}</a>
                </h3>
                <h6>
                    <a href="#">
                        <i className="fa fa-angle-double-left"/>
                        <small>НА ГЛАВНУЮ</small>
                    </a>
                </h6>
            </div>
        );
    }
}

class AuthBlock extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isAuthorized: false,
            nickname: null
        };
    }

    render() {
        return (
            <div className="AuthBlock offset-md-6 col-md-3">
                {this.content()}
            </div>
        );
    }

    content() {
        if (this.state.isAuthorized) {
            return (
                <div className="auth">
                    <a href="#"><i className="fa fa-user"/>
                        <span>{this.state.nickname}</span>
                    </a>
                    <a href="#">
                        <i className="fa fa-sign-out"/>
                        <span>ВЫЙТИ</span>
                    </a>
                </div>
            );
        } else {
            return (
                <div className="auth">
                    <a href="#"><i className="fa fa-sign-in"/>ВОЙТИ</a>
                </div>
            );
        }
    }
}

export default Header;