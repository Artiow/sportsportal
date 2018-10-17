import React from 'react';
import apiUrl from '../../boot/constants';
import {withFrameContext} from '../../boot/frame/MainFrame';
import ContentContainer from '../../util/components/special/ContentContainer';
import ContentRow from '../../util/components/special/ContentRow';
import StarRate from '../../util/components/StarRate';
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
        const orderList = (reservations, title) => {
            const list = [];
            if (reservations) {
                reservations.forEach((value, index) => {
                    const playground = value.playground;
                    list.push(
                        <ContentRow className="details">
                            <div className="col-4">
                                <h2 className="mb-1">{playground.name}</h2>
                                <h4 className="mb-2">{playground.address}</h4>
                                <h6><StarRate value={playground.rate}/></h6>
                            </div>
                            <div className="col-8">
                                <code className="receipt">
                                    {title}
                                    <br/>Позиция #{index + 1}
                                    <hr className="my-2"/>
                                    Объект: {playground.name}
                                    <br/>Адрес: {playground.address}
                                    <br/>...
                                    <br/>...
                                    <br/>...
                                    <br/>...
                                    <br/>...
                                    <hr className="my-2"/>
                                    Итого: {value.totalPrice}<i className="fa fa-rub ml-1"/>
                                </code>
                            </div>
                        </ContentRow>
                    )
                })
            }
            return list;
        };
        const order = this.state.content;
        const title = order ? `Заказ #${orderId(order.id)}` : null;
        return order ? (
            <ContentContainer className="OrderInfo">
                <ContentRow className="header">
                    <div className="col-12">
                        <h1 className="mb-1">{title}</h1>
                        <h4>статус: {orderStatus(order.paid, order.byOwner)}</h4>
                    </div>
                </ContentRow>
                {orderList(order ? order.reservations : null, title)}
                <ContentRow>
                    <div className="col-12">
                        <h4>JSON</h4>
                        <code>{JSON.stringify(order)}</code>
                    </div>
                </ContentRow>
            </ContentContainer>
        ) : (null);
    }
})