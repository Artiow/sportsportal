import React from "react";
import {withRouter} from "react-router-dom";
import Authentication from "../../connector/Authentication";
import {withApplicationContext} from "../Application";
import {env} from "../constants";
import ModalFade from "../../util/components/ModalFade";
import MainContainer from "./sections/MainContainer";
import Login from "./modal/Login";
import MessageRegistration from "./modal/MessageRegistration";
import MessageRecover from "./modal/MessageRecover";
import Registration from "./modal/Registration";
import Recovery from "./modal/Recovery";
import Reset from "./modal/Reset";
import Header from "./sections/Header";
import Footer from "./sections/Footer";
import "./MainFrame.css";

const MainFrameContext = React.createContext(null);

export function withMainFrameContext(Component) {
    return function ContextualComponent(props) {
        return (
            <MainFrameContext.Consumer>
                {mainframe => <Component {...props} mainframe={mainframe}/>}
            </MainFrameContext.Consumer>
        )
    }
}

export default withApplicationContext(withRouter(
    class MainFrame extends React.Component {

        static MODAL = Object.freeze({
            LOGIN: "LOGIN",
            RECOVERY: "RECOVERY",
            REGISTRATION: "REGISTRATION",
            RESET: "RESET"
        });

        constructor(props) {
            super(props);
            this.state = {
                principal: this.props.application.principal,
                showLogin: this.showLoginModal.bind(this)
            }
        }

        static switch(currentModal, nextModal) {
            setTimeout(() => nextModal.activate(), env.ANIMATION_TIMEOUT);
            currentModal.activate('hide', env.ANIMATION_TIMEOUT);
        }

        componentDidMount() {
            if (localStorage.getItem('pre_login')) {
                localStorage.removeItem('pre_login');
                this.showLoginModal();
            }
        }

        componentWillReceiveProps(nextProps) {
            this.setState({principal: nextProps.application.principal})
        }

        login() {
            this.loginModal.activate('hide', env.ANIMATION_TIMEOUT);
            this.props.application.login();
        }

        logout() {
            Authentication.logout()
                .then(() => {
                    console.debug('MainFrame', 'logout', 'logout successful');
                    this.props.application.logout();
                    if (this.props.location.pathname !== '/') {
                        this.props.application.preLogin();
                    } else {
                        this.showLoginModal();
                    }
                })
                .catch(() => {
                    console.debug('MainFrame', 'logout', 'logout failed');
                })
        }

        showLoginModal(event) {
            if (event != null) event.preventDefault();
            this.loginModal.activate();
        }

        reShowLoginModalFromRegistrationModal() {
            MainFrame.switch(this.registrationModal, this.loginModal);
        }

        reShowLoginModalFromRecoveryModal() {
            MainFrame.switch(this.recoveryModal, this.loginModal);
        }

        reShowRegistrationModal() {
            MainFrame.switch(this.loginModal, this.registrationModal);
        }

        reShowRecoveryModal() {
            MainFrame.switch(this.loginModal, this.recoveryModal);
        }

        render() {
            return (
                <MainFrameContext.Provider
                    value={this.state}>
                    <div className="MainFrame">
                        <Header {...this.props.header}
                                username={this.state.principal ? this.state.principal.name : undefined}
                                onLogin={this.showLoginModal.bind(this)}
                                onLogout={this.logout.bind(this)}/>
                        <MainContainer{...this.props.main}>
                            {this.props.children}
                        </MainContainer>
                        <Footer {...this.props.footer}/>
                        <LoginModal ref={modal => this.loginModal = modal}
                                    onRegistrationClick={this.reShowRegistrationModal.bind(this)}
                                    onRecoveryClick={this.reShowRecoveryModal.bind(this)}
                                    onSuccess={this.login.bind(this)}/>
                        <RegistrationModal ref={modal => this.registrationModal = modal}
                                           onLogClick={this.reShowLoginModalFromRegistrationModal.bind(this)}
                                           onSuccess={undefined}/>
                        <RecoveryModal ref={modal => this.recoveryModal = modal}
                                       onLogClick={this.reShowLoginModalFromRecoveryModal.bind(this)}
                                       onSuccess={undefined}/>
                    </div>
                </MainFrameContext.Provider>
            )
        }
    }
))

class LoginModal extends React.Component {

    activate(options, timeout) {
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        if (this.body) {
            setTimeout(() => this.body.reset(), timeout ? timeout : 0)
        }
    }

    render() {
        return (
            <ModalFade className="ModalFade" ref={modal => this.modal = modal}>
                <div className="modal-dialog modal-sm">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Авторизация
                            </h5>
                            <button type="button"
                                    className="close"
                                    data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(env.ANIMATION_TIMEOUT)
                                    }}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <Login ref={body => this.body = body}
                                   onRegistrationClick={this.props.onRegistrationClick}
                                   onRecoveryClick={this.props.onRecoveryClick}
                                   onSuccess={this.props.onSuccess}/>
                        </div>
                    </div>
                </div>
            </ModalFade>
        );
    }
}

class RegistrationModal extends React.Component {

    static STAGE = Object.freeze({REGISTRATION: 1, MESSAGE: 2});

    constructor(props) {
        super(props);
        this.state = {
            stage: RegistrationModal.STAGE.REGISTRATION,
            userId: null
        }
    }

    activate(options, timeout) {
        this.setState({
            stage: RegistrationModal.STAGE.REGISTRATION,
            userId: null
        });
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        if (this.body) {
            setTimeout(() => this.body.reset(), timeout ? timeout : 0)
        }
    }

    sendMessage(userId) {
        this.setState({
            stage: RegistrationModal.STAGE.MESSAGE,
            userId: userId
        });
    }

    render() {
        return (
            <ModalFade className="ModalFade" ref={modal => this.modal = modal}>
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Регистрация
                            </h5>
                            <button type="button"
                                    className="close"
                                    data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(env.ANIMATION_TIMEOUT)
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
                                                          onLogClick={this.props.onLogClick}
                                                          onSuccess={this.sendMessage.bind(this)}/>
                                        );
                                    case RegistrationModal.STAGE.MESSAGE:
                                        return (
                                            <MessageRegistration onSuccess={this.props.onSuccess}
                                                                 recipientId={this.state.userId}/>
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

class RecoveryModal extends React.Component {

    static STAGE = Object.freeze({RECOVERY: 1, MESSAGE: 2});

    constructor(props) {
        super(props);
        this.state = {
            stage: RecoveryModal.STAGE.RECOVERY,
            userEmail: null
        }
    }

    activate(options, timeout) {
        this.setState({
            stage: RecoveryModal.STAGE.RECOVERY,
            userEmail: null
        });
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        if (this.body) {
            setTimeout(() => this.body.reset(), timeout ? timeout : 0)
        }
    }

    sendMessage(userEmail) {
        this.setState({
            stage: RecoveryModal.STAGE.MESSAGE,
            userEmail: userEmail
        });
    }

    render() {
        return (
            <ModalFade className="ModalFade" ref={modal => this.modal = modal}>
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Восстановление доступа
                            </h5>
                            <button type="button"
                                    className="close"
                                    data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(env.ANIMATION_TIMEOUT)
                                    }}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            {(() => {
                                switch (this.state.stage) {
                                    case RecoveryModal.STAGE.RECOVERY:
                                        return (
                                            <Recovery ref={body => this.body = body}
                                                      onLogClick={this.props.onLogClick}
                                                      onSuccess={this.sendMessage.bind(this)}/>
                                        );
                                    case RecoveryModal.STAGE.MESSAGE:
                                        return (
                                            <MessageRecover onSuccess={this.props.onSuccess}
                                                            recipientEmail={this.state.userEmail}/>
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

class ResetModal extends React.Component {

    constructor(props) {
        super(props);
    }

    activate(options, timeout) {
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        if (this.body) {
            setTimeout(() => this.body.reset(), timeout ? timeout : 0)
        }
    }

    render() {
        return (
            <ModalFade className="ModalFade" ref={modal => this.modal = modal}>
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title">
                                Восстановление доступа
                            </h5>
                            <button type="button"
                                    className="close"
                                    data-dismiss="modal"
                                    onClick={event => {
                                        this.reset(env.ANIMATION_TIMEOUT)
                                    }}>
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <Reset ref={body => this.body = body}
                                   onSuccess={this.props.onSuccess}/>
                        </div>
                    </div>
                </div>
            </ModalFade>
        );
    }
}