import React, {Component} from 'react';
import apiUrl from '../constants'
import axios from 'axios';
import qs from 'qs';
import './Confirmation.css';

class Confirmation extends Component {

    static UNEXPECTED_ERROR_MESSAGE = 'Непредвиденная ошибка!';
    static STAGE = Object.freeze({PROCESSED: 1, SUCCESS: 2, FAILED: 3});

    constructor(props) {
        super(props);
        this.state = {
            stage: Confirmation.STAGE.PROCESSED,
            errorMessage: null
        };
        this.confirm();
    }

    confirm() {
        const self = this;
        const confirmToken = qs.parse(this.props.location.search, {ignoreQueryPrefix: true}).token;
        console.log('Accepted Token:', confirmToken);
        axios
            .put(apiUrl('/auth/confirm'), '', {
                params: {confirmToken: confirmToken},
                paramsSerializer: (params => qs.stringify(params))
            })
            .then(function (response) {
                console.log('Confirmation Response:', response);
                self.setState({stage: Confirmation.STAGE.SUCCESS});
            })
            .catch(function (error) {
                const errorResponse = error.response;
                console.log('Confirmation Error:', ((errorResponse != null) ? errorResponse : error));
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
                        <div className="stage stage-processed">Загрузка...</div>
                    );
                case Confirmation.STAGE.SUCCESS:
                    window.location.replace('/login');
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

export default Confirmation;