import React from 'react';
import {Link} from 'react-router-dom';
import {withApplicationContext} from '../Application';
import Authentication from '../../connector/Authentication';
import qs from 'qs';
import './Confirmation.css';

export default withApplicationContext(class Confirmation extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка';
    static STAGE = Object.freeze({PROCESSED: 1, SUCCESS: 2, FAILED: 3});

    constructor(props) {
        super(props);
        this.state = {
            stage: Confirmation.STAGE.PROCESSED,
            errorMessage: null
        };
    }

    componentDidMount() {
        this.confirmQuery();
    }

    confirmQuery() {
        Authentication.doConfirmation(qs.parse(this.props.location.search, {ignoreQueryPrefix: true}).token)
            .then(response => {
                this.setState({
                    stage: Confirmation.STAGE.SUCCESS
                });
            })
            .catch(error => {
                this.setState({
                    stage: Confirmation.STAGE.FAILED,
                    errorMessage: error ? error.message : Confirmation.UNEXPECTED_ERROR_MESSAGE
                });
            })
    }

    render() {
        return (
            <div className="Confirmation">
                <div className="container">{(() => {
                    switch (this.state.stage) {
                        case Confirmation.STAGE.PROCESSED:
                            return (
                                <div className="stage stage-processed">
                                    <div className="progress">
                                        <div className="progress-bar progress-bar-striped progress-bar-animated"
                                             aria-valuenow="100" aria-valuemin="0" aria-valuemax="100"
                                             style={{width: '100%'}}/>
                                    </div>
                                </div>
                            );
                        case Confirmation.STAGE.SUCCESS:
                            this.props.application.preLogin();
                            return (null);
                        case Confirmation.STAGE.FAILED:
                            return (
                                <div className="stage stage-failed alert alert-danger">
                                    <h4 className="alert-heading">Не удалось подтвердить вашу учетную запись!</h4>
                                    <p>Произошла ошибка при подтверждении вашей учетной записи.</p>
                                    <hr/>
                                    <p>{this.state.errorMessage}! <Link to={'/'} className="alert-link">Вернуться на
                                        главную
                                        страницу</Link>.</p>
                                </div>
                            );
                        default:
                            return (null);
                    }
                })()}</div>
            </div>);
    }
})