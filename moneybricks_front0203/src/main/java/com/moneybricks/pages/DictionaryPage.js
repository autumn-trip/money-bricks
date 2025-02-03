import React from "react";
import DictionaryComponent from "../components/dictionary/DictionaryComponent"; //

import "../styles/dictionary/DictionaryPage.scss";

const DictionaryPage = () => {
    return (
        <div>
            <h1>Dictionary Page</h1>
            <DictionaryComponent />
        </div>
    );
};

export default DictionaryPage;