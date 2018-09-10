import React, {Component} from "react";
import './Main.css';

class Main extends Component {
    render() {
        return (
            <main className="Main container">
                <div className="row">
                    <PlaygroundFilter/>
                    <PageablePlaygroundContainer content={{}}/>
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
    if (content.length > 0) {
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
            container.push(<PlaygroundCard playground={item}/>);
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
    const photoURL = ((photoURLs.length > 0) ? (photoURLs[0] + '?size=sm') : '%PUBLIC_URL%/resources/img/no-image-sm');
    return (
        <div className="col-xs-12 col-sm-6 mb-4">
            <div className="card">
                <img className="card-img" src={photoURL}/>
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
                    <a href={"/lease/playground/id" + playground.id} className="btn btn-outline-info btn-sm">
                        Подробнее...
                    </a>
                </div>
            </div>
        </div>
    );
}

class Rate extends Component {
    constructor(props) {
        super(props);
    }

    static buildRate(rate) {

    }

    render() {
        const rate = this.props.rate;
        let stars = [];
        let i = 0;

        while (i <= (rate - 2)) {
            stars.push(<i className="fa fa-star"/>);
            i += 2;
        }
        if (i < rate) {
            stars.push(<i className="fa fa-star-half-o"/>);
            i += 2;
        }
        while (i < 10) {
            stars.push(<i className="fa fa-star-o"/>);
            i += 2;
        }

        return (
            <h6 className="card-title">
                <small className="card-rate">{stars}</small>
            </h6>
        );
    }
}

export default Main;