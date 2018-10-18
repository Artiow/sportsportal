import React from 'react';
import apiUrl, {env} from '../../boot/constants';
import {withFrameContext} from '../../boot/frame/MainFrame';
import placeimg from '../../util/img/no-image-grey-sm.jpg';
import ContentContainer from '../../util/components/special/ContentContainer';
import ContentRow from '../../util/components/special/ContentRow';
import PlacedImg from '../../util/components/PlacedImg';
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
        const orderStatusComponent = (paid, owner) => {
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
        const orderListComponent = (reservations, title) => {
            const orderList = (reservations, halfHourAvailable, viewPrice) => {
                const list = [];
                const reservationMap = new Map();
                reservations.forEach(value => {
                    const key = value.datetime.split('T')[0];
                    if (reservationMap.has(key)) {
                        const obj = reservationMap.get(key);
                        obj.price = obj.price + value.price;
                        obj.count = obj.count + 1;
                    } else reservationMap.set(key, {
                        price: value.price,
                        count: 1
                    });
                });
                reservationMap.forEach((value, key) => {
                    const datetime = key;
                    const day = (env.DAYS_OF_WEEK_NAMES[new Date(datetime).getDay()]).short;
                    const date = datetime.split('T')[0].replace(new RegExp('-', 'g'), '.');
                    const divider = halfHourAvailable ? 2 : 1;
                    const mainTime = Math.floor(value.count / divider);
                    const subTime = (halfHourAvailable && ((value.count % divider) !== 0)) ? '30' : '00';
                    const time = subTime ? `${mainTime} ч. ${subTime} мин.` : `${mainTime} ч.`;
                    list.push(
                        <span key={key}>
                            <br/>{`${date} [${day}] всего ${time}`}
                            {(() => viewPrice ? (
                                <span className="ml-2">на сумму {value.price}<i className="fa fa-rub ml-1"/></span>
                            ) : (null))()}
                        </span>
                    )
                });
                return list;
            };
            const list = [];
            if (reservations) {
                reservations.forEach((value, index) => {
                    const playground = value.playground;
                    list.push(
                        <ContentRow key={index} className="details">
                            <div className="col-4">
                                <h2 className="mb-1">{playground.name}</h2>
                                <h4 className="mb-2">{playground.address}</h4>
                                <h6><StarRate value={playground.rate}/></h6>
                                <PlacedImg className="card-img mt-2"
                                           src={`${playground.photoURLs[0]}?size=sm`}
                                           placeImg={placeimg} alt={playground.name}/>
                            </div>
                            <div className="col-8">
                                <code className="receipt">
                                    {title}
                                    <br/>Позиция #{index + 1}
                                    <hr className="my-2"/>
                                    Объект: {playground.name}
                                    <br/>Адрес: {playground.address}
                                    {orderList(value.reservations, playground.halfHourAvailable, (value.totalPrice > 0))}
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
                        <h4>статус: {orderStatusComponent(order.paid, order.byOwner)}</h4>
                    </div>
                </ContentRow>
                {orderListComponent(order ? order.reservations : null, title)}
                {!order.paid ? (
                    <ContentRow>
                        <div className="col-12">
                            <div className="btn-group">
                                <button className="btn btn-danger disabled" disabled={true}>
                                    Отменить заказ
                                </button>
                                <button className="btn btn-success disabled" disabled={true}>
                                    Оплатить все
                                    <span className="badge badge-dark ml-1">
                                        {order.price}<i className="fa fa-rub ml-1"/>
                                    </span>
                                </button>
                            </div>
                        </div>
                    </ContentRow>
                ) : (null)}
                <ContentRow>
                    <div className="col-12">
                        <h4>JSON</h4>
                        <code style={{whiteSpace: 'pre'}}>{JSON.stringify(order, null, 2)}</code>
                    </div>
                </ContentRow>
            </ContentContainer>
        ) : (null);
    }
})