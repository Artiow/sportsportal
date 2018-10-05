import React from 'react';
import MainContainer from './components/MainContainer';
import Header from './components/Header';
import Footer from './components/Footer';

export default function MainFrame(props) {
    return (
        <div className="MainFrame">
            <Header {...props.header}/>
            <MainContainer {...props.main}>
                {props.children}
            </MainContainer>
            <Footer {...props.footer}/>
        </div>
    )
}