import React from "react";
import InputMask from "react-input-mask";
import {Link} from "react-router-dom";
import "./Recovery.css";

export default class Recovery extends React.Component {

    static DEFAULT_MESSAGE = 'Пожалуйста, заполните регистрационные данные';
    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';

    constructor(props) {
        super(props);
        this.state = {
            errorMessage: null,
            errorMessages: {},
            inProcess: false
        }
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            errorMessage: null,
            errorMessages: {},
            inProcess: false
        });
    }

    queryRecovery(body) {
        this.setState({
            errorMessage: null,
            errorMessages: {},
            inProcess: true
        });
        // todo: put recovery-init method call here!
        console.debug('Recovery', 'query', 'success');
        const onSuccess = this.props.onSuccess;
        if (typeof onSuccess === 'function') onSuccess(body.email);
    }

    render() {
        return (
            <div className="Recovery">
                <RecoveryForm inProcess={this.state.inProcess}
                              ref={form => this.submitForm = form}
                              errorMessage={this.state.errorMessage}
                              errorMessages={this.state.errorMessages}
                              onSubmit={this.queryRecovery.bind(this)}
                              onLogClick={this.props.onLogClick}/>
            </div>
        );
    }
}

class RecoveryForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            email: '',
            errorMessage: ((props.errorMessage != null) ? props.errorMessage : null),
            errorMessages: ((props.errorMessages != null) ? props.errorMessages : {})
        }
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            email: '',
            errorMessage: ((this.props.errorMessage != null) ? this.props.errorMessage : null),
            errorMessages: ((this.props.errorMessages != null) ? this.props.errorMessages : {})
        });
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            errorMessage: ((nextProps.errorMessage != null) ? nextProps.errorMessage : null),
            errorMessages: ((nextProps.errorMessages != null) ? nextProps.errorMessages : {})
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.setState({errorMessage: null, errorMessages: {}});
        const onSubmit = this.props.onSubmit;
        if (typeof onSubmit === 'function') onSubmit({
            email: this.state.email,
        });
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({[target.name]: target.value});
    }

    handleLogClick(event) {
        event.preventDefault();
        const onLogClick = !this.props.inProcess ? this.props.onLogClick : null;
        if (typeof onLogClick === 'function') onLogClick(event);
    }

    render() {
        const error = !(this.state.errorMessage == null);
        return (
            <form className="RecoveryForm"
                  onSubmit={this.handleSubmit.bind(this)}
                  ref={form => this.submitForm = form}>
                <div className={`alert alert-${error ? 'warning' : 'light'}`}>
                    {error ? this.state.errorMessage : Recovery.DEFAULT_MESSAGE}
                </div>
                <div className="form-row-main-container">
                    <div className="form-row-container">
                        <InputField identifier={'email'} placeholder={'Email'}
                                    type={'email'} autoComplete={'email'}
                                    errorMessage={this.state.errorMessages.email}
                                    onChange={this.handleInputChange.bind(this)}
                                    value={this.state.email}
                                    required={'required'}/>
                    </div>
                </div>
                <div className="row">
                    <div className="col-sm-6 offset-sm-3">
                        <button className="btn btn-primary btn-lg btn-block"
                                disabled={this.props.inProcess} type="submit">
                            {(!this.props.inProcess) ? (
                                <span>Восстановить</span>
                            ) : (
                                <i className="fa fa-refresh fa-spin fa-fw"/>
                            )}
                        </button>
                        <div className="login">
                            <Link to="/login"
                                  onClick={this.handleLogClick.bind(this)}>
                                Авторизация
                            </Link>
                        </div>
                    </div>
                </div>
            </form>
        )
    }
}

function InputField(props) {
    const errorMessage = Array.isArray(props.errorMessage) ? props.errorMessage[0] : null;
    const error = ((errorMessage != null) && (errorMessage !== ''));
    return (
        <div className="form-row">
            <label htmlFor={props.identifier} className="col-sm-3 col-form-label">
                {props.placeholder}
            </label>
            <div className="col-sm-9">
                <InputMask id={`reg_form_${props.identifier}`}
                           type={(props.type != null) ? props.type : 'text'}
                           name={props.identifier}
                           placeholder={props.placeholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           mask={props.mask}
                           value={props.value}
                           autoComplete={props.autoComplete}
                           onChange={props.onChange}
                           required={props.required}/>
                <div style={(!error) ? {display: 'none'} : {display: 'block'}}
                     className="invalid-feedback">
                    {errorMessage}
                </div>
            </div>
        </div>
    );
}