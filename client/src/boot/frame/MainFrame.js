import React from 'react';
import ModalFade from '../../util/components/ModalFade'
import MainContainer from './sections/MainContainer';
import Login from './modal/Login'
import Header from './sections/Header';
import Footer from './sections/Footer';

class MainFrame extends React.Component {
    static reLogin() {
        localStorage.setItem('re_login', true);
        window.location.replace('/');
    }

    componentDidMount() {
        if (localStorage.getItem('re_login')) {
            this.showLoginModal();
            localStorage.removeItem('re_login');
        }
    }

    showLoginModal() {
        this.loginForm.show();
    }

    hideLoginModal() {
        this.loginForm.show('hide');
    }

    render() {
        return (
            <div className="MainFrame">
                <Header {...this.props.header}
                        onLogin={this.showLoginModal.bind(this)}
                        onLogout={MainFrame.reLogin}/>
                <MainContainer{...this.props.main}>
                    {this.props.children}
                </MainContainer>
                <Footer {...this.props.footer}/>
                <LoginModal ref={modal => this.loginForm = modal}
                            onRegClick={this.hideLoginModal.bind(this)}/>
            </div>
        )
    }
}

class LoginModal extends React.Component {
    show(options) {
        this.modal.show(options);
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

export default MainFrame;