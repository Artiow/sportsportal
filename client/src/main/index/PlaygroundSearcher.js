import React from 'react';
import PlaygroundFilter from './PlaygroundFilter';
import PlaygroundPageableContainer from './PlaygroundPageableContainer';
import apiUrl from '../../boot/constants';
import axios from 'axios';
import qs from 'qs';

export default class PlaygroundSearcher extends React.Component {

    static DEFAULT_PAGE_SIZE = 10;

    updateFilter = newFilter => {
        this.filter = Object.assign(this.filter, newFilter);
        this.query(this.filter);
    };

    constructor(props) {
        super(props);
        this.state = {page: null};
        this.filter = {pageNum: 0, pageSize: PlaygroundSearcher.DEFAULT_PAGE_SIZE};
        this.query(this.filter);
    }

    /**
     * Downloading playground page.
     * @param filter {object} playground filter
     */
    query(filter) {
        const self = this;
        const url = apiUrl('/leaseapi/playground/list');
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
            <div className="PlaygroundSearcher row">
                <PlaygroundFilter onChange={this.updateFilter}/>
                <PlaygroundPageableContainer page={this.state.page}/>
            </div>
        );
    }
}