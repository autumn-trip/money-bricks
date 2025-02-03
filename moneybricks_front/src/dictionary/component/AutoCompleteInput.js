import React, { useState, useEffect } from 'react';

const AutoCompleteInput = ({ suggestions = [], onSearch }) => {
    const [inputValue, setInputValue] = useState(""); // 입력 값 상태
    const [showSuggestions, setShowSuggestions] = useState(false); // 추천어 표시 여부

    // 입력 값 변경 핸들러
    const handleInputChange = (e) => {
        const value = e.target.value;
        setInputValue(value);

        if (value.trim() === "") {
            setShowSuggestions(false);
            console.log("빈 입력 값으로 onSearch 실행 방지");
        } else {
            setShowSuggestions(true);
            console.log("AutoCompleteInput에서 검색어 전달:", value); // 디버깅용 로그
            onSearch(value);
        }
    };
    // 추천어 클릭 핸들러
    const handleSuggestionClick = (suggestion) => {
        if (!suggestion || !suggestion.dictionaryTerms.trim()) {
            console.log("잘못된 추천어 선택 방지");
            return; // 추천어가 비어 있거나 잘못된 경우 처리하지 않음
        }
        setInputValue(suggestion.dictionaryTerms); // 선택된 추천어로 입력 값 설정
        setShowSuggestions(false);
        onSearch(suggestion.dictionaryTerms); // 선택된 추천어로 검색 실행
    };

    // 추천어 목록 렌더링
    const renderSuggestions = () => {
        if (!showSuggestions || !Array.isArray(suggestions) || suggestions.length === 0) {
            return null;
        }

        return (
            <ul className="suggestions-list">
                {suggestions.map((suggestion) => (
                    <li
                        key={suggestion.dictionaryId}
                        onClick={() => handleSuggestionClick(suggestion)}
                    >
                        {suggestion.dictionaryTerms}
                    </li>
                ))}
            </ul>
        );
    };

    // 디바운스 효과로 검색어 변경 지연
    useEffect(() => {
        const debounceTimeout = setTimeout(() => {
            if (inputValue.trim() !== "") {
                console.log("AutoCompleteInput에서 onSearch 호출:", inputValue); // 디버깅용 로그
                onSearch(inputValue);
            }
        }, 300); // 300ms 지연

        return () => clearTimeout(debounceTimeout);
    }, [inputValue, onSearch]);

    return (
        <div
            className="autocomplete-input"
            onBlur={() => setTimeout(() => setShowSuggestions(false), 100)}
        >
            <input
                type="text"
                value={inputValue}
                placeholder="Search terms..."
                onChange={handleInputChange}
                onFocus={() => setShowSuggestions(true)}
            />
            {renderSuggestions()}
        </div>
    );
};

export default AutoCompleteInput;
