import React from 'react';
import apiUrl from '../../boot/constants';
import {withFrameContext} from '../../boot/frame/MainFrame';
import axios from 'axios';
import './OrderInfo.css';

export default withFrameContext(class OrderInfo extends React.Component {

    static UUID_LENGTH = 10;

    constructor(props) {
        super(props);
        this.identifier = this.props.identifier;
        this.state = {content: null};
    }

    componentDidMount() {
        this.query(this.props.main.user.token);
    }

    componentWillReceiveProps(nextProps) {
        this.query(nextProps.main.user.token);
    }

    query(token) {
        if (token) {
            axios.get(
                apiUrl('/leaseapi/order/' + this.identifier), {
                    headers: {Authorization: token}
                }
            ).then(response => {
                console.debug('OrderInfo (query):', response);
                this.setState({content: response.data});
            }).catch(error => {
                console.error('OrderInfo (query):', ((error.response != null) ? error.response : error));
            })
        }
    }

    render() {
        const orderId = id => {
            let result = '' + id;
            while (result.length < OrderInfo.UUID_LENGTH)
                result = '0' + result;
            return result;
        };
        const orderStatus = (paid, owner) => {
            let text = 'ошибка';
            let styleClass = 'danger';
            if (paid && owner) {
                text = 'зарезервировано';
                styleClass = 'primary';
            } else if (paid && !owner) {
                text = 'оплачено';
                styleClass = 'success';
            } else if (!paid && !owner) {
                text = 'не оплачено';
                styleClass = 'warning';
            }
            return (<span className={`badge badge-${styleClass}`}>{text}</span>)
        };
        const order = this.state.content;
        return order ? (
            <div className="OrderInfo row">
                <div className="col-10 offset-1">
                    <div className="container content-container">
                        <div className="row header-row">
                            <div className="col-12">
                                <h1 className="row-h header-h header-name">
                                    {`Заказ #${orderId(order.id)}`}
                                </h1>
                                <h4 className="row-h header-h header-address">
                                    статус: {orderStatus(order.paid, order.byOwner)}
                                </h4>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-12">
                                <code>{JSON.stringify(this.state.content)}</code>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        ) : (null);
    }
})