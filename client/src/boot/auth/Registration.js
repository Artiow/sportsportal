import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import InputMask from 'react-input-mask';
import 'jquery';
import './Registration.css';

// todo: look validation here: https://github.com/Artiow/SPORTSPORTAL/commit/7795e03bd69da1390050d7c9b3b3db166ecf105a#diff-cd3c7ffc29eedb5555cda9c8b564a457
class Registration extends Component {
    constructor(props) {
        super(props);
    }

    handleSubmit(event) {
        event.preventDefault();
    }

    render() {
        return (
            <div className="Registration">
                <div className="container">
                    <form onSubmit={this.handleSubmit.bind(this)}>
                        <h1>Регистрация</h1>
                        <div className="alert alert-warning"/>
                        <InputField identifier={'name'} placeholder={'Имя'}/>
                        <InputField identifier={'surname'} placeholder={'Фамилия'}/>
                        <InputField identifier={'patronymic'} placeholder={'Отчество'}/>
                        <InputField identifier={'address'} placeholder={'Адрес'}/>
                        <InputField identifier={'phone'} placeholder={'Телефон'}
                                    maskChar=" " mask="+7 (999) 999 99 99"/>
                        <InputField identifier={'login'} placeholder={'Логин'}/>
                        <DoubleInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                          secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}/>
                        <div className="row">
                            <div className="col-sm-6 offset-sm-3">
                                <button type="submit" className="btn btn-primary btn-lg btn-block">Регистрация</button>
                                <div className="login-link-box">
                                    <Link className="btn btn-link btn-sm" to="/login">Авторизация</Link>
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
            error: false,
            errorMessage: null
        };
    }

    render() {
        return (
            <div className="form-row">
                <label htmlFor={this.props.identifier} className="col-sm-3 col-form-label">
                    {this.props.placeholder}
                </label>
                <div className="col-sm-9">
                    <InputMask type="text" id={this.props.identifier}
                               className={(!this.state.error) ? 'form-control' : 'form-control is-invalid'}
                               placeholder={this.props.placeholder}
                               maskChar={this.props.maskChar} mask={this.props.mask}
                               required="required"/>
                    <div style={(!this.state.error) ? {display: 'none'} : {display: 'block'}}
                         className="invalid-feedback">
                        {this.state.errorMessage}
                    </div>
                </div>
            </div>
        );
    }
}

class DoubleInputField extends Component {
    constructor(props) {
        super(props);
        this.state = {
            error: false,
            errorMessage: null
        };
    }

    render() {
        return (
            <div className="form-row-container">
                <div className="form-row">
                    <label htmlFor={this.props.firstIdentifier} className="col-sm-3 col-form-label">
                        {this.props.firstPlaceholder}
                    </label>
                    <div className="col-sm-9">
                        <input type="password" id={this.props.firstIdentifier}
                               className={(!this.state.error) ? 'form-control' : 'form-control is-invalid'}
                               placeholder={this.props.firstPlaceholder}
                               required="required"/>
                    </div>
                </div>
                <div className="form-row">
                    <label htmlFor={this.props.secondIdentifier} className="col-sm-3 col-form-label"/>
                    <div className="col-sm-9">
                        <input type="password" id={this.props.secondIdentifier}
                               className={(!this.state.error) ? 'form-control' : 'form-control is-invalid'}
                               placeholder={this.props.secondPlaceholder}
                               required="required"/>
                        <div style={(!this.state.error) ? {display: 'none'} : {display: 'block'}}
                             className="invalid-feedback">
                            {this.state.errorMessage}
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default Registration;