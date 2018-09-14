import React, {Component} from "react";
import {Link} from 'react-router-dom';
import {getApiUrl} from '../boot/constants'
import {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import axios from 'axios';
import './Main.css';
import noImageSm from '../util/img/no-image-sm.jpg';

class Main extends Component {

    static PAGE_SIZE = 10;

    updateFilterCallback = newFilter => {
        this.setState(prevState => {
            return {filter: Object.assign(prevState.filter, newFilter)}
        });
        this.query();
    };

    constructor(props) {
        super(props);
        this.state = {
            content: null,
            pageNumber: 0,
            totalPages: 0,
            filter: {
                pageNum: 0,
                pageSize: Main.PAGE_SIZE
            }
        };
        this.query();
    }

    query() {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/list');
        axios.get(url, {params: this.state.filter})
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
                    <PlaygroundFilter callback={this.updateFilterCallback}/>
                    <PageablePlaygroundContainer pageable={(this.state.totalPages > 1)}
                                                 content={this.state.content}/>
                </div>
            </main>
        );
    }
}

class PlaygroundFilter extends Component {

    static MAX_COST = 1000000;

    updateCostCallback = range => {
        const MAX = PlaygroundFilter.MAX_COST;
        this.setState({
            startCost: (MAX * (range[0] / 100)),
            endCost: (MAX * (range[1] / 100))
        });
    };

    constructor(props) {
        super(props);
        this.state = {
            searchString: null,
            opening: '00:00',
            startCost: 0,
            endCost: PlaygroundFilter.MAX_COST
        }
    }

    handleInputChange(event) {
        const target = event.target;
        this.setState({
            [target.id]: target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.callback({
            searchString: this.state.searchString
        })
    }

    render() {
        return (
            <div className="PlaygroundFilter col-xs-12 col-sm-4 mb-4">
                <form onSubmit={this.handleSubmit.bind(this)} className="card">
                    <div className="input-group mb-3">
                        <input id="searchString" onChange={this.handleInputChange.bind(this)}
                               type="text" className="form-control"/>
                        <div className="input-group-append">
                            <button className="btn btn-outline-secondary" type="submit">Найти</button>
                        </div>
                    </div>
                    <div id="accordion">
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <button className="btn btn-link" data-toggle="collapse" data-target="#collapse_1">
                                        Стоимость часа
                                    </button>
                                </h5>
                            </div>
                            <div id="collapse_1" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span
                                                className="badge-param">{(this.state.startCost / 100).toFixed()}</span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">{(this.state.endCost / 100).toFixed()}</span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <Range allowCross={false} defaultValue={[0, 100]}
                                           onBeforeChange={this.updateCostCallback}
                                           onAfterChange={this.updateCostCallback}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <button className="btn btn-link" data-toggle="collapse" data-target="#collapse_2">
                                        Время работы
                                    </button>
                                </h5>
                            </div>
                            <div id="collapse_2" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">

                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">

                                        </span>
                                    </h6>
                                    <Range allowCross={false} defaultValue={[0, 100]}/>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}

function PageablePlaygroundContainer(props) {
    const content = props.content;
    const pageable = props.pageable;
    if ((content !== null) && (content.length > 0)) {
        return (
            <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
                <PlaygroundContainer content={content}/>
                {(pageable) ? (<PlaygroundPagination/>) : (null)}
            </div>
        );
    } else {
        return (
            <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
                <div className="col-xs-12 col-sm-12 mb-12">
                    <div className="alert alert-primary">Ничего не найдено!</div>
                </div>
            </div>
        );
    }
}

function PlaygroundPagination(props) {
    return (
        <div className="PlaygroundPagination row">
            <div className="col-xs-12 col-sm-12 mb-4">
                <div className="card" style={{minHeight: '50px'}}/>
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
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : noImageSm);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}/>
                <div className="card-body">
                    <h4 className="card-title">
                        <small>{playground.name}</small>
                    </h4>
                    <Rate className="card-title" rate={playground.rate}/>
                    <p className="card-text">
                        <span className="badge badge-dark">
                            от<span>{((playground.cost / 100).toFixed())}</span><i className="fa fa-rub"/>/час
                        </span>
                    </p>
                    <Link to={"/playground/id" + playground.id} className="btn btn-outline-info btn-sm">
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
        <h6 className="Rate card-title">{stars}</h6>
    );
}

export default Main;