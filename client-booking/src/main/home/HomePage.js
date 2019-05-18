import {withMainFrameContext} from "../../boot/frame/MainFrame";
import React from "react";
import ContentContainer from "../../util/components/special/ContentContainer";

export default withMainFrameContext(class HomePage extends React.Component {
    render() {
        return (<ContentContainer className="HomePage"/>);
    }
})