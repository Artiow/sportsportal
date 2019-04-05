import React from 'react';
import PlaygroundFilter from './components/PlaygroundFilter';
import PlaygroundPageableContainer from './components/PlaygroundPageableContainer';
import Playground from '../../connector/Playground';

export default class PlaygroundSearcher extends React.Component {

    static DEFAULT_PAGE_SIZE = 10;

    constructor(props) {
        super(props);
        this.state = {page: null, loading: true};
        this.filter = {pageNum: 0, pageSize: PlaygroundSearcher.DEFAULT_PAGE_SIZE};
    }

    updateFilter = newFilter => {
        this.filter = Object.assign(this.filter, newFilter);
        this.query(this.filter);
    };

    componentDidMount() {
        this.query(this.filter);
    }

    /**
     * Downloading playground page.
     * @param filter {object} playground filter
     */
    query(filter) {
        this.setState({loading: true});
        Playground.list(filter)
            .then(data => {
                console.debug('PlaygroundSearcher', 'query', data);
                this.setState({page: data, loading: false});
            })
            .catch(error => {
                console.error('PlaygroundSearcher', 'query', error);
            });
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