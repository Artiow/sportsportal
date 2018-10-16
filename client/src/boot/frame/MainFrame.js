import React from 'react';
import verify from "../../util/verification";
import ModalFade from '../../util/components/ModalFade';
import MainContainer from './sections/MainContainer';
import Login from './modal/Login';
import Message from './modal/Message';
import Registration from './modal/Registration'
import Header from './sections/Header';
import Footer from './sections/Footer';

export default class MainFrame extends React.Component {

    static ANIMATION_TIMEOUT = 300;

    constructor(props) {
        super(props);
        this.state = {
            credential: null
        }
    }

    static reLogin(event) {
        if (event != null) event.preventDefault();
        localStorage.setItem('re_login', true);
        window.location.replace('/');
    }

    static switch(currentModal, nextModal) {
        setTimeout(() => nextModal.activate(), MainFrame.ANIMATION_TIMEOUT);
        currentModal.activate('hide');
    }

    componentDidMount() {
        if (localStorage.getItem('re_login')) {
            localStorage.removeItem('re_login');
            this.showLoginModal();
        } else {
            this.queryVerify();
        }
    }

    queryLogout() {
        this.setState({
            isAuthorized: false,
            nickname: null
        });
        localStorage.clear();
        MainFrame.reLogin()
    }

    queryVerify() {
        verify(
            data => this.setState({credential: data.login.username}),
            error => this.setState({credential: null})
        );
    }

    showLoginModal() {
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
            <div className="MainFrame">
                <Header {...this.props.header}
                        credential={this.state.credential}
                        onLogin={this.showLoginModal.bind(this)}
                        onLogout={this.queryLogout.bind(this)}/>
                <MainContainer{...this.props.main}>
                    {this.props.children}
                </MainContainer>
                <Footer {...this.props.footer}/>
                <LoginModal ref={modal => this.loginForm = modal}
                            onRegClick={this.reShowRegistrationModal.bind(this)}/>
                <RegistrationModal ref={modal => this.registrationForm = modal}
                                   onLogClick={this.reShowLoginModal.bind(this)}/>
            </div>
        )
    }
}

class LoginModal extends React.Component {

    activate(options) {
        this.modal.activate(options);
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
                            <button type="button" className="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            <Login onRegClick={this.props.onRegClick}/>
                        </div>
                    </div>
                </div>
            </ModalFade>
        );
    }
}

class RegistrationModal extends React.Component {

    static STAGE = Object.freeze({REGISTRATION: 1, MESSAGE: 2});
    static INIT_STAGE = RegistrationModal.STAGE.REGISTRATION;

    constructor(props) {
        super(props);
        this.state = {stage: RegistrationModal.INIT_STAGE}
    }

    activate(options) {
        this.setState({stage: RegistrationModal.INIT_STAGE});
        this.modal.activate(options);
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
                            <button type="button" className="close" data-dismiss="modal">
                                <span>&times;</span>
                            </button>
                        </div>
                        <div className="modal-body">
                            {(() => {
                                switch (this.state.stage) {
                                    case RegistrationModal.STAGE.REGISTRATION:
                                        return (
                                            <Registration onSuccess={this.sendMessage.bind(this)}
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