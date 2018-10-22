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
        this.state = {page: null, loading: true};
        this.filter = {pageNum: 0, pageSize: PlaygroundSearcher.DEFAULT_PAGE_SIZE};
    }

    componentDidMount() {
        this.query(this.filter);
    }

    /**
     * Downloading playground page.
     * @param filter {object} playground filter
     */
    query(filter) {
        this.setState({loading: true});
        axios.get(apiUrl('/leaseapi/playground/list'), {
            paramsSerializer: params => qs.stringify(params, {arrayFormat: 'repeat'}),
            params: filter
        }).then(response => {
            console.debug('PlaygroundSearcher (query):', response);
            this.setState({page: response.data, loading: false});
        }).catch(error => {
            console.error('PlaygroundSearcher (query):', ((error.response != null) ? error.response : error));
        })
    }

    render() {
        return (
            <div className="PlaygroundSearcher row">
                <PlaygroundFilter inProcess={this.state.loading}
                                  onChange={this.updateFilter}/>
                <PlaygroundPageableContainer content={this.state.page}
                                             loading={this.state.loading}/>
            </div>
        );
    }
}