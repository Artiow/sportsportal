import React from "react";
import {withApplicationContext} from "../Application";
import Authentication from "../../connector/Authentication";
import "./Reset.css";
import qs from "qs";
import {Link} from "react-router-dom";

export default withApplicationContext(class Reset extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';
    static STAGE = Object.freeze({PROCESSED: 1, SUCCESS: 2, FAILED: 3});

    constructor(props) {
        super(props);
        this.state = {
            token: null,
            stage: null,
            errorMessage: null,
            errorMessages: {},
            inProcess: false
        }
    }

    componentDidMount() {
        this.setState({
            token: qs.parse(this.props.location.search, {ignoreQueryPrefix: true}).token
        });
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            errorMessage: null,
            errorMessages: {},
            inProcess: false
        });
    }

    queryReset(body) {
        this.setState({
            errorMessage: null,
            errorMessages: {},
            inProcess: true
        });
        Authentication.doRecovery(this.state.token, body.password)
            .then(response => {
                this.setState({
                    stage: Reset.STAGE.SUCCESS
                });
            })
            .catch(error => {
                if (error) {
                    const message = error.message;
                    const errors = error.errors;
                    console.warn('Reset', 'query', errors ? 'invalid form data' : 'failed');
                    this.setState({
                        errorMessage: message,
                        errorMessages: errors,
                        inProcess: false
                    });
                } else {
                    console.error('Reset', 'query', 'failed');
                    this.setState({
                        errorMessage: Reset.UNEXPECTED_ERROR_MESSAGE,
                        inProcess: false
                    });
                }
            });
    }

    render() {
        return (
            <div className="Reset">
                <div className="container">
                    {(this.state.stage == null) ? (
                        <ResetForm inProcess={this.state.inProcess}
                                   ref={form => this.submitForm = form}
                                   errorMessage={this.state.errorMessage}
                                   errorMessages={this.state.errorMessages}
                                   onSubmit={this.queryReset.bind(this)}/>
                    ) : (() => {
                        switch (this.state.stage) {
                            case Reset.STAGE.PROCESSED:
                                return (
                                    <div className="stage stage-processed">
                                        <div className="progress">
                                            <div className="progress-bar progress-bar-striped progress-bar-animated"
                                                 aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"
                                                 style={{width: '100%'}}/>
                                        </div>
                                    </div>
                                );
                            case Reset.STAGE.SUCCESS:
                                this.props.application.preLogin();
                                return (null);
                            case Reset.STAGE.FAILED:
                                return (
                                    <div className="stage stage-failed alert alert-danger">
                                        <h4 className="alert-heading">Не удалось подтвердить вашу учетную запись!</h4>
                                        <p>Произошла ошибка при подтверждении вашей учетной записи.</p>
                                        <hr/>
                                        <p>{this.state.errorMessage}!</p>
                                        <p><Link to={'/'} className="alert-link">Вернуться на главную страницу</Link>.
                                        </p>
                                    </div>
                                );
                            default:
                                return (null);
                        }
                    })()}
                </div>
            </div>
        );
    }
})

class ResetForm extends React.Component {

    static DEFAULT_MESSAGE = 'Введите новый пароль';

    constructor(props) {
        super(props);
        this.state = {
            password: '',
            confirm: '',
            errorMessage: ((props.errorMessage != null) ? props.errorMessage : null),
            errorMessages: ((props.errorMessages != null) ? props.errorMessages : {})
        }
    }

    reset() {
        this.submitForm.reset();
        this.setState({
            password: '',
            confirm: '',
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
        if (this.state.password === this.state.confirm) {
            const onSubmit = this.props.onSubmit;
            if (typeof onSubmit === 'function') onSubmit({
                password: this.state.password
            });
        } else {
            this.setState({errorMessages: {password: ['Введенные пароли не совпадают!']}});
        }
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({[target.name]: target.value});
    }

    render() {
        const error = !(this.state.errorMessage == null);
        return (
            <form className="ResetForm"
                  onSubmit={this.handleSubmit.bind(this)}
                  ref={form => this.submitForm = form}>
                <div className={`alert alert-${error ? 'warning' : 'light'}`}>
                    {error ? this.state.errorMessage : ResetForm.DEFAULT_MESSAGE}
                </div>
                <div className="form-row-main-container">
                    <PasswordInputField firstIdentifier={'password'} firstPlaceholder={'Пароль'}
                                        secondIdentifier={'confirm'} secondPlaceholder={'Подтвердите пароль'}
                                        errorMessage={this.state.errorMessages.password}
                                        onChange={this.handleInputChange.bind(this)}
                                        required={'required'}/>
                </div>
                <div className="row">
                    <div className="offset-sm-3 offset-md-4 col-sm-6 col-md-4">
                        <button className="btn btn-primary btn-lg btn-block"
                                disabled={this.props.inProcess} type="submit">
                            {(!this.props.inProcess) ? (
                                <span>Сохранить</span>
                            ) : (
                                <i className="fa fa-refresh fa-spin fa-fw"/>
                            )}
                        </button>
                    </div>
                </div>
            </form>
        )
    }
}

function PasswordInputField(props) {
    const errorMessage = Array.isArray(props.errorMessage) ? props.errorMessage[0] : null;
    const error = ((errorMessage != null) && (errorMessage !== ''));
    return (
        <div className="form-row-container">
            <div className="form-row">
                <div className="offset-sm-3 offset-md-4 col-sm-6 col-md-4">
                    <input id={`reg_form_${props.firstIdentifier}`}
                           type="password" autoComplete="new-password"
                           name={props.firstIdentifier} placeholder={props.firstPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                </div>
            </div>
            <div className="form-row">
                <div className="offset-sm-3 offset-md-4 col-sm-6 col-md-4">
                    <input id={`reg_form_${props.secondIdentifier}`}
                           type="password" autoComplete="new-password"
                           name={props.secondIdentifier} placeholder={props.secondPlaceholder}
                           className={(!error) ? 'form-control' : 'form-control is-invalid'}
                           onChange={props.onChange}
                           required={props.required}/>
                    <div style={(!error) ? {display: 'none'} : {display: 'block'}}
                         className="invalid-feedback">
                        {errorMessage}
                    </div>
                </div>
            </div>
        </div>
    );
}