import React, {Component} from "react";
import {Link} from 'react-router-dom';
import axios from 'axios';
import './Main.css';
import noImageSm from '../util/img/no-image-sm.jpg';

class Main extends Component {
    constructor(props) {
        super(props);
        this.state = {
            content: null,
            pageNumber: null,
            totalPages: null,
            filter: {
                pageNum: 0,
                pageSize: 6
            }
        };

        this.query();
    }

    updateData() {

    }

    query() {
        const self = this;
        axios.get('http://localhost:8080/leaseapi/playground/list', {params: this.state.filter})
            .then(function (response) {
                console.log('Response:', response);
                const data = response.data;
                self.setState({
                    content: data.content,
                    pageNumber: data.pageNumber,
                    totalPages: data.totalPages
                });
            })
            .catch(function (error) {
                console.log('Error:', error);
            })
    }

    render() {
        return (
            <main className="Main container">
                <div className="row">
                    <PlaygroundFilter/>
                    <PageablePlaygroundContainer content={this.state.content}/>
                </div>
            </main>
        );
    }
}

function PlaygroundFilter(props) {
    return (
        <div className="col-xs-12 col-sm-4 mb-4">
            <div className="card">
                <div style={{height: '500px'}}/>
            </div>
        </div>
    );
}

function PageablePlaygroundContainer(props) {
    const content = props.content;
    console.log('New Content:', content);
    if ((content !== null) && (content.length > 0)) {
        return (
            <div className="col-xs-12 col-sm-8">
                <PlaygroundContainer content={content}/>
                <PlaygroundPagination/>
            </div>
        );
    } else {
        return (
            <div className="col-xs-12 col-sm-8">
                <div className="col-xs-12 col-sm-12 mb-12">
                    <div className="alert alert-primary">Ничего не найдено!</div>
                </div>
            </div>
        );
    }
}

function PlaygroundPagination(props) {
    return (
        <div className="row">
            <div className="col-xs-12 col-sm-12 mb-4">
                <div className="card">
                    <div style={{height: '50px'}}/>
                </div>
            </div>
        </div>
    );
}

function PlaygroundContainer(props) {
    const content = props.content;
    let container = [];
    if (content.length > 0) {
        content.forEach(function (item, i, arr) {
            container.push(<PlaygroundCard key={i} playground={item}/>);
        });
    } else {
        container.push(
            <div className="col-xs-12 col-sm-12 mb-12">
                <div className="alert alert-primary">Ничего не найдено!</div>
            </div>
        );
    }
    return (<div className="row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : noImageSm);
    return (
        <div className="col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}/>
                <div className="card-body">
                    <h4 className="card-title">
                        <small>{playground.name}</small>
                    </h4>
                    <Rate className="card-title" rate={playground.rate}/>
                    <p className="card-text">
                        <span className="badge badge-dark">
                            {'от' + ((playground.cost / 100).toFixed())}<i className="fa fa-rub"/>/час
                        </span>
                    </p>
                    <Link to={"/lease/playground/id" + playground.id} className="btn btn-outline-info btn-sm">
                        Подробнее...
                    </Link>
                </div>
            </div>
        </div>
    );
}

function Rate(props) {
    const rate = props.rate;
    let stars = [];

    let i = 0;
    while (i <= (rate - 2)) {
        stars.push(<i key={i} className="fa fa-star"/>);
        i += 2;
    }
    if (i < rate) {
        stars.push(<i key={i} className="fa fa-star-half-o"/>);
        i += 2;
    }
    while (i < 10) {
        stars.push(<i key={i} className="fa fa-star-o"/>);
        i += 2;
    }

    return (
        <h6 className="card-title">
            <small className="card-rate">{stars}</small>
        </h6>
    );
}

export default Main;