import React from 'react';
import apiUrl from '../../boot/constants';
import axios from 'axios';
import './OrderInfo.css';
import {withFrameContext} from "../../boot/frame/MainFrame";

export default withFrameContext(class OrderInfo extends React.Component {
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
        return (
            <div className="OrderInfo row">
                <div className="col-10 offset-1">
                    <code>{JSON.stringify(this.state.content)}</code>
                </div>
            </div>
        );
    }
})