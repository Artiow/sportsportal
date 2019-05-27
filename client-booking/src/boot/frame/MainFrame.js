import React from "react";
import {withRouter} from "react-router-dom";
import Authentication from "../../connector/Authentication";
import {withApplicationContext} from "../Application";
import {env} from "../constants";
import ModalFade from "../../util/components/ModalFade";
import MainContainer from "./sections/MainContainer";
import Login from "./modal/Login";
import Message from "./modal/Message";
import Registration from "./modal/Registration";
import Header from "./sections/Header";
import Footer from "./sections/Footer";

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
            this.loginForm.activate('hide', env.ANIMATION_TIMEOUT);
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
                        <LoginModal ref={modal => this.loginForm = modal}
                                    onRegistrationClick={this.reShowRegistrationModal.bind(this)}
                                    onRecoveryClick={this.reShowRegistrationModal.bind(this)}
                                    onSuccess={this.login.bind(this)}/>
                        <RegistrationModal ref={modal => this.registrationForm = modal}
                                           onLogClick={this.reShowLoginModal.bind(this)}
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
        if (this.body) setTimeout(() => {
            this.body.reset()
        }, timeout ? timeout : 0)
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
            userEmail: null,
            userId: null
        }
    }

    activate(options, timeout) {
        this.setState({
            stage: RegistrationModal.STAGE.REGISTRATION,
            userEmail: null,
            userId: null
        });
        this.modal.activate(options);
        if (options) this.reset(timeout);
    }

    reset(timeout) {
        if (this.body) setTimeout(() => {
            this.body.reset()
        }, timeout ? timeout : 0)
    }

    sendMessage(userId, userEmail) {
        this.setState({
            stage: RegistrationModal.STAGE.MESSAGE,
            userEmail: userEmail,
            userId: userId
        });
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
                                            <Message onSuccess={this.props.onSuccess}
                                                     recipientEmail={this.state.userEmail}
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