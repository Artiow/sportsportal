import React, {Component} from "react";
import Slider, {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import {Link} from 'react-router-dom';
import {getApiUrl} from '../boot/constants'
import StarRate from '../util/components/StarRate';
import CustomCheckbox from '../util/components/CustomCheckbox';
import axios from 'axios';
import qs from 'qs';
import './Index.css';
import noImage from '../util/img/no-image-white-sm.jpg';

class Index extends Component {

    static DEFAULT_PAGE_SIZE = 10;

    updateFilter = newFilter => {
        this.filter = Object.assign(this.filter, newFilter);
        this.query(this.filter);
    };

    constructor(props) {
        super(props);
        this.state = {page: null};
        this.filter = {pageNum: 0, pageSize: Index.DEFAULT_PAGE_SIZE};
        this.query(this.filter);
    }

    /**
     * Downloading playground page.
     * @param filter {object} playground filter
     */
    query(filter) {
        const self = this;
        const url = getApiUrl('/leaseapi/playground/list');
        const serializer = params => {
            return qs.stringify(params, {arrayFormat: 'repeat'})
        };
        axios.get(url, {
            params: filter,
            paramsSerializer: serializer
        }).then(function (response) {
            console.log('Query Response:', response);
            self.setState({page: response.data});
        }).catch(function (error) {
            console.log('Query Error:', ((error.response != null) ? error.response : error));
        })
    }

    render() {
        return (
            <div className="row">
                <PlaygroundFilter callback={this.updateFilter}/>
                <PageablePlaygroundContainer page={this.state.page}/>
            </div>
        );
    }
}

class PlaygroundFilter extends Component {

    static MIN_PRICE = 0;
    static MAX_PRICE = 10000;
    static PRICE_STEP = 100;

    updateRateCallback = slider => {
        this.setState({
            minRate: slider
        });
    };

    updatePriceCallback = range => {
        this.setState({
            startPrice: range[0],
            endPrice: range[1]
        });
    };

    updateTimeCallback = range => {
        const normalize = time => {
            const normalTime = (time !== 48) ? time : 0;
            const timeHour = Math.floor(normalTime / 2);
            const timeMinute = (30 * (normalTime % 2));
            return ((timeHour < 10) ? ('0' + timeHour) : timeHour) + ':' + ((timeMinute !== 30) ? (timeMinute + '0') : timeMinute)
        };
        this.setState({
            opening: normalize(range[0]),
            closing: normalize(range[1])
        });
    };

    constructor(props) {
        super(props);
        this.dictionary = {
            sports: null,
            features: null,
        };
        this.state = {
            sportCodes: [],
            featureCodes: [],
            searchString: '',
            startPrice: PlaygroundFilter.MIN_PRICE,
            endPrice: PlaygroundFilter.MAX_PRICE,
            opening: '00:00',
            closing: '00:00',
            minRate: 0
        };

        const self = this;
        this.uploadFilerData("/leaseapi/dict/feature/list", function (response) {
            self.dictionary.features = response;
        });
        this.uploadFilerData("/leaseapi/dict/sport/list", function (response) {
            self.dictionary.sports = response;
        });
    }

    static updateCheckboxArray(codes, code, checked) {
        const idx = codes.indexOf(code);
        if ((checked) && (idx < 0)) codes.push(code);
        else codes.splice(idx, 1);
        return codes;
    }

    /**
     * Load dictionary and store it in state.
     * @param uri {string}
     * @param setting {function(object)}
     */
    uploadFilerData(uri, setting) {
        axios.get(getApiUrl(uri))
            .then(function (response) {
                console.log('Dictionary Response:', response);
                setting(response.data.content);
            })
            .catch(function (error) {
                console.log('Dictionary Error:', ((error.response != null) ? error.response : error));
            })
    }

    handleInputChange(event) {
        this.setState({
            searchString: event.target.value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.callback({
            searchString: this.state.searchString,
            featureCodes: this.state.featureCodes,
            sportCodes: this.state.sportCodes,
            startPrice: this.state.startPrice,
            endPrice: this.state.endPrice,
            opening: this.state.opening,
            closing: this.state.closing,
            minRate: this.state.minRate
        })
    }

    updateSportCodes(code, checked) {
        this.setState(prevState => {
            return {sportCodes: PlaygroundFilter.updateCheckboxArray(prevState.sportCodes, code, checked)}
        });
    }

    updateFeatureCodes(code, checked) {
        this.setState(prevState => {
            return {featureCodes: PlaygroundFilter.updateCheckboxArray(prevState.featureCodes, code, checked)}
        });
    }

    render() {
        const setCheckboxData = (prefix, content, updater) => {
            const result = [];
            if ((content != null) && (content.length > 0)) {
                content.forEach(function (item, i, arr) {
                    result.push(
                        <CustomCheckbox key={i} id={prefix + '_' + item.code} value={item.code}
                                        label={item.name.charAt(0).toUpperCase() + item.name.slice(1)}
                                        onChange={event => {
                                            updater(event.target.value, event.target.checked);
                                        }}/>
                    );
                });
            }
            return result;
        };
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
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_1">
                                        Виды спорта
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_1" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    {setCheckboxData('sport', this.dictionary.sports, this.updateSportCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_2">
                                        Инфраструктура
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_2" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    {setCheckboxData('feature', this.dictionary.features, this.updateFeatureCodes.bind(this))}
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_3">
                                        Стоимость часа
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_3" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {Math.floor(this.state.startPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">
                                                {Math.floor(this.state.endPrice)}
                                            </span>
                                            <i className="fa fa-rub"/>/час
                                        </span>
                                    </h6>
                                    <Range allowCross={false}
                                           min={PlaygroundFilter.MIN_PRICE} max={PlaygroundFilter.MAX_PRICE}
                                           defaultValue={[PlaygroundFilter.MIN_PRICE, PlaygroundFilter.MAX_PRICE]}
                                           step={PlaygroundFilter.PRICE_STEP}
                                           onChange={this.updatePriceCallback}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_4">
                                        Время работы
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_4" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6>
                                        <span className="badge-sub">от</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">{this.state.opening}</span>
                                        </span>
                                    </h6>
                                    <h6>
                                        <span className="badge-sub">до</span>
                                        <span className="badge badge-dark">
                                            <span className="badge-param">{this.state.closing}</span>
                                        </span>
                                    </h6>
                                    <Range min={0} max={48}
                                           allowCross={false} defaultValue={[0, 48]}
                                           onChange={this.updateTimeCallback}/>
                                </div>
                            </div>
                        </div>
                        <div className="card">
                            <div className="card-header">
                                <h5 className="mb-0">
                                    <a className="btn btn-link" data-toggle="collapse" data-target="#collapse_5">
                                        Рейтинг
                                    </a>
                                </h5>
                            </div>
                            <div id="collapse_5" className="collapse" data-parent="#accordion">
                                <div className="card-body">
                                    <h6 className="badge badge-dark">
                                            <span className="badge-param">
                                                <StarRate value={this.state.minRate}/>
                                            </span>
                                    </h6>
                                    <Slider min={0} max={10} defaultValue={0}
                                            onChange={this.updateRateCallback}/>
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
    const page = props.page;
    const totalPages = (page != null) ? page.totalPages : 0;
    const content = (page != null) ? page.content : null;
    return ((content !== null) && (content.length > 0)) ? (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
            <PlaygroundContainer content={content}/>
            {(totalPages > 1) ? (<PlaygroundPagination/>) : (null)}
        </div>
    ) : (((content !== null) && (content.length === 0)) ? (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8">
            <div className="col-xs-12 col-sm-12 mb-12">
                <div className="alert alert-light">
                    <h4 className="alert-heading">Ничего не найдено!</h4>
                    <hr/>
                    <p className="mb-0">Не существует таких площадок, которые удовлетворяли бы запросу.</p>
                </div>
            </div>
        </div>
    ) : (
        <div className="PageablePlaygroundContainer col-xs-12 col-sm-8"/>
    ));
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
    let container = [];
    const content = props.content;
    if ((content !== null) && (content.length > 0)) {
        content.forEach(function (item, i, arr) {
            container.push(<PlaygroundCard key={i} playground={item}/>);
        });
    }
    return (<div className="PlaygroundContainer row">{container}</div>);
}

function PlaygroundCard(props) {
    const playground = props.playground;
    const photoURLs = playground.photoURLs;
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : noImage);
    return (
        <div className="PlaygroundCard col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL} alt={playground.name}/>
                <div className="card-body">
                    <h4 className="card-title">
                        <small>{playground.name}</small>
                    </h4>
                    <h6 className="card-title">
                        <StarRate value={playground.rate}/>
                    </h6>
                    <p className="card-text">
                        <span className="badge badge-dark">
                            от<span>{Math.floor(playground.price)}</span><i className="fa fa-rub"/>/час
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

export default Index;