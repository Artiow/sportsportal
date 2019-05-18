import React from 'react';
import {env} from '../../boot/constants';
import {withMainFrameContext} from '../../boot/frame/MainFrame';
import Order from '../../connector/Order';
import placeimg from '../../util/img/no-image-grey-sm.jpg';
import ContentContainer from '../../util/components/special/ContentContainer';
import ContentRow from '../../util/components/special/ContentRow';
import PlacedImg from '../../util/components/PlacedImg';
import StarRate from '../../util/components/StarRate';
import Timer from '../../util/components/Timer';
import './OrderPage.css';

export default withMainFrameContext(class OrderPage extends React.Component {

    static UUID_LENGTH = 10;

    constructor(props) {
        super(props);
        this.id = props.identifier;
        this.state = {
            content: null,
            link: null,
        };
    }

    componentDidMount() {
        Order.get(this.id)
            .then(data => {
                console.info('OrderPage', 'query', 'success');
                this.setState({content: data});
            })
            .catch(error => {
                console.error('OrderPage', 'query', 'failed');
            });
    }

    render() {
        const orderId = id => {
            let result = '' + id;
            while (result.length < OrderPage.UUID_LENGTH)
                result = '0' + result;
            return result;
        };
        const orderStatusComponent = (paid, owner, freed) => {
            let text = 'ошибка';
            let styleClass = 'danger';
            if (owner && paid) {
                text = 'зарезервировано';
                styleClass = 'primary';
            } else if (freed && paid) {
                text = 'забронировано';
                styleClass = 'success';
            } else if (paid) {
                text = 'оплачено';
                styleClass = 'success';
            } else if (!paid) {
                text = 'не оплачено';
                styleClass = 'warning';
            } else {
                text = 'не определен';
                styleClass = 'danger';
            }
            return (<span className={`badge badge-${styleClass}`}>{text}</span>)
        };
        const orderListComponent = (reservations, title) => {
            const orderLine = (reservations, isHalfHour, isPriced) => {
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
                    const count = value.count;
                    const time = isHalfHour
                        ? `${Math.floor(count / 2)} ч. ${(isHalfHour && ((count % 2) !== 0)) ? '30' : '00'} мин.`
                        : `${count} ч.`;
                    list.push(
                        <span key={key}>
                            <br/>{`${date} [${day}] всего ${time}`}
                            {(() => isPriced ? (
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
                                    {orderLine(value.reservations, playground.halfHourAvailable, (value.totalPrice > 0))}
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
        const link = order ? order.paymentLink : null;
        const title = order ? `Заказ #${orderId(order.id)}` : null;
        const expiration = order ? order.expiration : null;
        let minute = 0;
        let second = 0;
        if (expiration) {
            const pre = (new Date(expiration)).getTime() - (new Date()).getTime();
            const res = (pre > 0) ? new Date(pre) : new Date(0);
            minute = res.getMinutes();
            second = res.getSeconds();
        }
        return order ? (
            <ContentContainer className="OrderPage">
                <ContentRow className="header">
                    <div className="col-12">
                        <h1 className="mb-1">{title}</h1>
                        <h4>статус: {orderStatusComponent(order.isPaid, order.isOwned, order.isFreed)}</h4>
                        {expiration ? (
                            <div className="alert alert-danger my-0">
                                <h4 className="alert-heading">Внимание!</h4>
                                Бронирование перестанет быть действительным
                                через <strong><Timer m={minute} s={second}/></strong>
                            </div>
                        ) : (null)}
                    </div>
                </ContentRow>
                {orderListComponent(order ? order.reservations : null, title)}
                <ContentRow>
                    <div className="col-12">
                        <div className="btn-group">
                            <button className="btn btn-danger disabled" disabled={true}>
                                Отменить {order.isOwned ? 'резервирование' : 'бронирование'}
                            </button>
                            {!order.isPaid && link ? (
                                <a href={link} className="btn btn-success">
                                    Оплатить все
                                    <span className="badge badge-dark ml-1">
                                        {order.sum}<i className="fa fa-rub ml-1"/>
                                    </span>
                                </a>
                            ) : (null)}
                        </div>
                    </div>
                </ContentRow>
            </ContentContainer>
        ) : (null);
    }
})