import React from 'react';
import {withRouter} from 'react-router-dom';
import {withAppContext} from '../Application';
import ModalFade from '../../util/components/ModalFade';
import MainContainer from './sections/MainContainer';
import Login from './modal/Login';
import Message from './modal/Message';
import Registration from './modal/Registration'
import Header from './sections/Header';
import Footer from './sections/Footer';

const FrameContext = React.createContext(null);

export function withFrameContext(Component) {
    return function ContextualComponent(props) {
        return (
            <FrameContext.Consumer>
                {main => <Component {...props} main={main}/>}
            </FrameContext.Consumer>
        )
    }
}

export default withAppContext(withRouter(
    class MainFrame extends React.Component {

        static ANIMATION_TIMEOUT = 300;

        constructor(props) {
            super(props);
            this.state = {
                user: MainFrame.calcUser(this.props.app.credentials),
                showLogin: this.showLoginModal.bind(this)
            }
        }

        static calcUser(credentials) {
            const isAuthorized = !!credentials;
            return {
                isAuthorized: isAuthorized,
                token: isAuthorized ? credentials.token : null,
                login: isAuthorized ? credentials.login : null
            }
        }

        static switch(currentModal, nextModal) {
            setTimeout(() => nextModal.activate(), MainFrame.ANIMATION_TIMEOUT);
            currentModal.activate('hide', MainFrame.ANIMATION_TIMEOUT);
        }

        componentDidMount() {
            if (localStorage.getItem('re_login')) {
                localStorage.removeItem('re_login');
                this.showLoginModal();
            }
        }

        componentWillReceiveProps(nextProps) {
            this.setState({
                user: MainFrame.calcUser(nextProps.app.credentials)
            })
        }

        queryLogin(data) {
            this.loginForm.activate('hide', MainFrame.ANIMATION_TIMEOUT);
            this.props.app.login(data);
        }

        queryLogout() {
            localStorage.clear();
            this.props.app.logout();
            if (this.props.location.pathname !== '/') {
                this.props.app.reLogin();
            } else {
                this.showLoginModal();
            }
        }

        showLoginModal(event) {
            if (event != null) event.preventDefault();
            this.loginForm.activate();
        }

        reShowLoginModal() {
            MainFrame.switch(this.registrationForm, this.loginForm);
        }

        reShowRegistrationModal() {
            MainFrame.switch(this.loginForm, this.registrationForm);
        }

        render() {
            return (
                <FrameContext.Provider
                    value={this.state}>
                    <div className="MainFrame">
                        <Header {...this.props.header}
                                onLogin={this.showLoginModal.bind(this)}
                                onLogout={this.queryLogout.bind(this)}
                                username={
                                    this.state.user.isAuthorized
                                        ? this.state.user.login.username
                                        : undefined
                                }/>
                        <MainContainer{...this.props.main}>
                            {this.props.children}
                        </MainContainer>
                        <Footer {...this.props.footer}/>
                        <LoginModal ref={modal => this.loginForm = modal}
                                    onSuccess={this.queryLogin.bind(this)}
                                    onRegClick={this.reShowRegistrationModal.bind(this)}/>
                        <RegistrationModal ref={modal => this.registrationForm = modal}
                                           onLogClick={this.reShowLoginModal.bind(this)}/>
                    </div>
                </FrameContext.Provider>
            )
        }
    }
))

class LoginModal extends React.Component {

    static ANIMATION_TIMEOUT = 300;

    activate(options, timeout) {
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        setTimeout(() => {
            this.body.reset()
        }, timeout ? timeout : 0);
    }

    render() {
        return (
            <ModalFade ref={modal => this.modal = modal}>
                <div className="modal-dialog modal-sm">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Авторизация
                            </h5>
                            <button type="button" className="close" data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(LoginModal.ANIMATION_TIMEOUT)
                                    }}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <Login ref={body => this.body = body}
                                   onSuccess={this.props.onSuccess}
                                   onRegClick={this.props.onRegClick}/>
                        </div>
                    </div>
                </div>
            </ModalFade>
        );
    }
}

class RegistrationModal extends React.Component {

    static ANIMATION_TIMEOUT = 300;

    static STAGE = Object.freeze({REGISTRATION: 1, MESSAGE: 2});
    static INIT_STAGE = RegistrationModal.STAGE.REGISTRATION;

    constructor(props) {
        super(props);
        this.state = {stage: RegistrationModal.INIT_STAGE}
    }

    activate(options, timeout) {
        this.setState({stage: RegistrationModal.INIT_STAGE});
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        setTimeout(() => {
            this.body.reset()
        }, timeout ? timeout : 0);
    }

    sendMessage(userId, userEmail) {
        this.setState({stage: RegistrationModal.STAGE.MESSAGE});
        this.messageForm.sendMessage(userId, userEmail);
    }

    render() {
        return (
            <ModalFade ref={modal => this.modal = modal}>
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Регистрация
                            </h5>
                            <button type="button" className="close" data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(RegistrationModal.ANIMATION_TIMEOUT)
                                    }}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            {(() => {
                                switch (this.state.stage) {
                                    case RegistrationModal.STAGE.REGISTRATION:
                                        return (
                                            <Registration ref={body => this.body = body}
                                                          onSuccess={this.sendMessage.bind(this)}
                                                          onLogClick={this.props.onLogClick}/>
                                        );
                                    case RegistrationModal.STAGE.MESSAGE:
                                        return (
                                            <Message ref={message => this.messageForm = message}/>
                                        );
                                    default:
                                        return null;
                                }
                            })()}
                        </div>
                    </div>
                </div>
            </ModalFade>
        );
    }
}