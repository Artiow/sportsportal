import React, {Component} from "react";
import 'rc-slider/assets/index.css';
import './Playground.css';
import {getApiUrl} from "../boot/constants";
import axios from "axios";

class Playground extends Component {
    constructor(props) {
        super(props);
        this.state = {
            id: props.identifier,
            content: null
        };
        this.queryOnLoad();
    }

    queryOnLoad() {
        const self = this;
        axios.get(
            getApiUrl('/leaseapi/playground/' + this.state.id)
        ).then(function (response) {
            console.log('Query Response:', response);
            self.setState({content: response.data});
        }).catch(function (error) {
            console.log('Query Error:', error);
        })
    }

    render() {
        return (
            <main className="Playground container">
                {(this.state.content != null) ? (
                    <h1 className="display-4">{this.state.content.name}</h1>
                ) : (null)}
            </main>
        );
    }
}

export default Playground;