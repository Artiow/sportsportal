import React, {Component} from 'react';
import InputMask from 'react-input-mask';
import 'jquery';
import './Registration.css';

class Registration extends Component {
    render() {
        return (
            <div className="Registration">
                <div className="container">
                    <form>
                        <h1>Регистрация</h1>
                        <div className="alert alert-warning"/>
                        <InputField identifier={'name'} placeholder={'Имя'}/>
                        <InputField identifier={'surname'} placeholder={'Фамилия'}/>
                        <InputField identifier={'patronymic'} placeholder={'Отчество'}/>
                        <InputField identifier={'address'} placeholder={'Адрес'}/>
                        <PhoneInputField identifier={'phone'} placeholder={'Телефон'}/>
                        <InputField identifier={'login'} placeholder={'Логин'}/>
                        <DoubleInputField id1={'password'} ph1={'Пароль'}
                                          id2={'confirm'} ph2={'Подтвердите пароль'}/>
                        <div className="row">
                            <div className="col-sm-6 offset-sm-3">
                                <button type="submit" className="btn btn-primary btn-lg btn-block">Регистрация</button>
                                <div className="login-link-box">
                                    <a className="btn btn-link btn-sm" href="#">Авторизация</a>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        );
    }
}

class InputField extends Component {
    constructor(props) {
        super(props);
        this.state = {
            identifier: props.identifier,
            placeholder: props.placeholder
        };
    }

    render() {
        const errorMessage = '';
        return (
            <div className="form-row">
                <label htmlFor={this.state.identifier} className="col-sm-3 col-form-label">
                    {this.state.placeholder}
                </label>
                <div className="col-sm-9">
                    <input type="text" id={this.state.identifier} className="form-control"
                           placeholder={this.state.placeholder}
                           required="required"/>
                    <div className="invalid-feedback">{errorMessage}</div>
                </div>
            </div>
        );
    }
}

class PhoneInputField extends Component {
    constructor(props) {
        super(props);
        this.state = {
            identifier: props.identifier,
            placeholder: props.placeholder
        };
    }

    render() {
        const errorMessage = '';
        return (
            <div className="form-row">
                <label htmlFor={this.state.identifier} className="col-sm-3 col-form-label">
                    {this.state.placeholder}
                </label>
                <div className="col-sm-9">
                    <InputMask type="text" id={this.state.identifier} className="form-control"
                               maskChar=' ' mask={'+7 (999) 999 99 99'}
                               placeholder={this.state.placeholder}
                               required="required"/>
                    <div className="invalid-feedback">{errorMessage}</div>
                </div>
            </div>
        );
    }
}

class DoubleInputField extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id1: props.id1,
            ph1: props.ph1,
            id2: props.id2,
            ph2: props.ph2
        };
    }

    render() {
        const errorMessage = '';
        return (
            <div className="form-row-container">
                <div className="form-row">
                    <label htmlFor={this.state.id1} className="col-sm-3 col-form-label">
                        {this.state.ph1}
                    </label>
                    <div className="col-sm-9">
                        <input type="password" id={this.state.id1} className="form-control"
                               placeholder={this.state.ph1}
                               required="required"/>
                    </div>
                </div>
                <div className="form-row">
                    <label htmlFor={this.state.id2} className="col-sm-3 col-form-label"/>
                    <div className="col-sm-9">
                        <input type="password" id={this.state.id2} className="form-control"
                               placeholder={this.state.ph2}
                               required="required"/>
                        <div className="invalid-feedback">{errorMessage}</div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Registration;