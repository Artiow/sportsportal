import {withMainFrameContext} from "../../boot/frame/MainFrame";
import React from "react";
import ContentContainer from "../../util/components/special/ContentContainer";
import ContentRow from "../../util/components/special/ContentRow";
import Order from "../../connector/Order";
import "./HomePage.css";
import {Link} from "react-router-dom";

export default withMainFrameContext(class HomePage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            content: null
        };
    }

    componentDidMount() {
        Order.getList()
            .then(data => {
                console.info('OrderList', 'query', 'success');
                this.setState({content: data});
            })
            .catch(error => {
                console.error('OrderList', 'query', 'failed');
            });
    }

    render() {
        const page = this.state.content;
        const orderRows = [];
        if (page !== null) {
            page.content.forEach(value => orderRows.push(<OrderRow order={value}/>));
        }
        return (
            <ContentContainer className="HomePage">
                <ContentRow className="header">
                    <div className="col-12">
                        <h1>Мои заказы</h1>
                    </div>
                </ContentRow>
                <div>{orderRows}</div>
            </ContentContainer>
        );
    }
})

function OrderRow(props) {
    const order = props.order;
    const status = (paid, owner, freed) => {
        if (owner && paid) {
            return <i className={`text-primary fa fa-check mr-2`}/>;
        } else if (paid) {
            return <i className={`text-success fa fa-check mr-2`}/>;
        } else if (!paid) {
            return <i className={`text-warning fa fa-exclamation mr-2`}/>;
        } else {
            return <i className={`text-danger fa fa-remove mr-2`}/>;
        }
    };
    return (
        <ContentRow>
            <Link className="col-12" to={`/order/id${order.id}`}>
                <h2 className="mb-0">{status(order.isPaid, order.isOwned, order.isFreed)}<span>{`Заказ #${order.id}`}</span>
                </h2>
            </Link>
        </ContentRow>
    );
}