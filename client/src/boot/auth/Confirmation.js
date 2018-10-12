import React from 'react';
import MainFrame from '../frame/MainFrame';
import apiUrl from '../constants'
import axios from 'axios';
import qs from 'qs';
import './Confirmation.css';

export default class Confirmation extends React.Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';
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
        const self = this;
        const confirmToken = qs.parse(this.props.location.search, {ignoreQueryPrefix: true}).token;
        console.log('Accepted Token (confirmation):', confirmToken);
        axios
            .put(apiUrl('/auth/confirm'), '', {
                params: {confirmToken: confirmToken},
                paramsSerializer: (params => qs.stringify(params))
            })
            .then(function (response) {
                console.log('Confirmation (query):', response);
                self.setState({stage: Confirmation.STAGE.SUCCESS});
            })
            .catch(function (error) {
                const errorResponse = error.response;
                console.error('Confirmation (query):', ((errorResponse != null) ? errorResponse : error));
                self.setState({
                    stage: Confirmation.STAGE.FAILED,
                    errorMessage: (errorResponse != null) ? errorResponse.data.message : Confirmation.UNEXPECTED_ERROR_MESSAGE
                });
            })
    }

    render() {
        const contentByStage = (stage) => {
            switch (stage) {
                case Confirmation.STAGE.PROCESSED:
                    return (
                        <div className="stage stage-processed">
                            <div className="progress">
                                <div className="progress-bar progress-bar-striped progress-bar-animated"
                                     aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style={{width: '100%'}}/>
                            </div>
                        </div>
                    );
                case Confirmation.STAGE.SUCCESS:
                    MainFrame.reLogin();
                    return (null);
                case Confirmation.STAGE.FAILED:
                    return (
                        <div className="stage stage-failed alert alert-danger">
                            <h4 className="alert-heading">Не удалось подтвердить вашу учетную запись!</h4>
                            <p>Произошла ошибка при подтверждении вашей учетной записи.</p>
                            <hr/>
                            <p>{this.state.errorMessage}</p>
                        </div>
                    );
                default:
                    return (null);
            }
        };
        return (
            <div className="Confirmation">
                <div className="container">{contentByStage(this.state.stage)}</div>
            </div>);
    }
}