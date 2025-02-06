import React, { useEffect, useState } from "react";
import "../style/NavDictionaryComponent.scss";
import {getRandomTerms} from "../../dictionary/api/dictionaryApi";

const DictionaryList = () => {
    const [terms, setTerms] = useState([]);

    useEffect(() => {
        const fetchTerms = async () => {
            try {
                const data = await getRandomTerms();
                setTerms(data);
            } catch (error) {
                console.error("Error fetching dictionary terms:", error);
            }
        };
        fetchTerms();
    }, []);

    return (
        <div className="dictionary-container">
            <h2>📖 오늘의 금융 용어</h2>
            <ul>
                {terms.map((term) => (
                    <li key={term.dictionaryId} className="dictionary-item">
                        <strong>{term.dictionaryTerms}</strong> - {term.dictionaryDefinitions}
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default DictionaryList;