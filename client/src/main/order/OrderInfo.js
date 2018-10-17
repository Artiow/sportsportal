import React from 'react';
import apiUrl from '../../boot/constants';
import {withFrameContext} from '../../boot/frame/MainFrame';
import ContentContainer from '../../util/components/special/ContentContainer';
import ContentRow from '../../util/components/special/ContentRow';
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
            <ContentContainer className="OrderInfo">
                <ContentRow className="header">
                    <div className="col-12">
                        <h1>{`Заказ #${orderId(order.id)}`}</h1>
                        <h4>статус: {orderStatus(order.paid, order.byOwner)}</h4>
                    </div>
                </ContentRow>
                <ContentRow>
                    <div className="col-12">
                        <code>{JSON.stringify(this.state.content)}</code>
                    </div>
                </ContentRow>
            </ContentContainer>
        ) : (null);
    }
})