import React from "react";
import Authentication from "../../../connector/Authentication";
import {env} from "../../constants";
import "./Message.css";

export default class MessageRegistration extends React.Component {

    static STAGE = Object.freeze({SENDING: 1, SUCCESS: 2, FAILED: 3});

    constructor(props) {
        super(props);
        this.state = {
            stage: MessageRegistration.STAGE.SENDING
        }
    }

    componentDidMount() {
        this.setState({
            stage: MessageRegistration.STAGE.SENDING,
        });
        this.sendMessage(
            this.props.recipientId
        );
    }

    componentWillReceiveProps(nextProps) {
        this.setState({
            stage: MessageRegistration.STAGE.SENDING
        });
        this.sendMessage(
            nextProps.recipientId
        );
    }

    sendMessage(recipientId) {
        Authentication.initConfirmation(
            recipientId, `${env.MAIN_HOST}/confirm`
        ).then(() => {
            console.debug('Message', 'sending', 'success');
            this.setState({stage: MessageRegistration.STAGE.SUCCESS})
        }).catch(() => {
            console.error('Message', 'sending', 'failed');
            this.setState({stage: MessageRegistration.STAGE.FAILED})
        });
    }

    render() {
        return (() => {
            switch (this.state.stage) {
                case MessageRegistration.STAGE.SENDING:
                    return (
                        <div className="Message alert">
                            <h4 className="alert-heading">Загрузка...</h4>
                            <hr/>
                            <div className="progress">
                                <div className="progress-bar progress-bar-striped progress-bar-animated"
                                     aria-valuenow="100" aria-valuemin="0" aria-valuemax="100" style={{width: '100%'}}/>
                            </div>
                        </div>
                    );
                case MessageRegistration.STAGE.SUCCESS:
                    return (
                        <div className="Message alert alert-success">
                            <h4 className="alert-heading">Подтвердите вашу учетную запись!</h4>
                            <p>Мы отправили на указанный вами почтовый ящик письмо с ссылкой для подтверждения вашей
                                учетной записи.</p>
                            <hr/>
                            <p>Пожалуйста, перейдите по ссылке, указанной в нем.</p>
                        </div>
                    );
                case MessageRegistration.STAGE.FAILED:
                    return (
                        <div className="Message alert alert-danger">
                            <h4 className="alert-heading">Не удалось подтвердить вашу учетную запись!</h4>
                            <p>Не удалось отправить на указанный вами почтовый письмо ящик с ссылкой для подтверждения
                                вашей учетной записи.</p>
                            <hr/>
                            <p className="alert-footer">Пожалуйста, проверьте правильность ввода данных.</p>
                        </div>
                    );
                default:
                    return null;
            }
        })();
    }
}