import React from 'react';
import './Footer.css';
import {getApiUrl} from '../constants'

function Footer(props) {
    return (
        <footer className="Footer">
            <div className="container">
                <div className="row">
                    <div className="col-md-6">
                        <h6><a href={getApiUrl('/')}>
                            <i className="fa fa-cogs"/>
                            <small>SportsPortal API</small>
                        </a></h6>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;