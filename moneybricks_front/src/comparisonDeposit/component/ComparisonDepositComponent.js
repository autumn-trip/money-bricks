import useCustomMove from "../../common/hooks/useCustomMove";
import {useEffect, useState} from "react";
import {getList} from "../api/comparisonDepositApi";
import "../style/ComparisonDepositComponent.scss";
import PageComponent from "../../common/component/PageComponent";
import {ProductType} from "./ProductType";

const initState = {
    dtoList: [],
    pageNumList: [],
    pageRequestDTO: null,
    prev: false,
    next: false,
    totalCount: 0,
    prevPage: 0,
    nextPage: 0,
    totalPage: 0,
    current: 0,
};

// 에러 처리
const handleError = (err) => {
    console.error("Error occurred:", err);
    alert("데이터를 불러오는 중 오류가 발생했습니다.");
};

const ComparisonDepositComponent = () => {
    const { page, size , moveToList } = useCustomMove();
    const [serverData, setServerData] = useState(initState);
    const [type, setType] = useState(ProductType.SAVINGS); // 초기값: 적금
    const [sortOption, setSortOption] = useState(""); // 정렬

    useEffect(() => {
        getList({ page, size }, type)
            .then((data) => {
                console.log("Fetched Data:", data);
                setServerData(data);
            })
            .catch((err) => handleError(err));
    }, [page, size, type]);

    // 예적금 변경
    const handleTypeChange = (newType) => {
        setType(newType);
    };

    // 데이터 정렬
    const sortedData = () => {
        console.log("Sort Option:", sortOption);
        return [...serverData.dtoList].sort((a, b) => {
            if (sortOption === "bankName") {
                return a.korCoNm.localeCompare(b.korCoNm); // 은행명 오름차순
            }
            if (sortOption === "basicRate") {
                return parseFloat(b.intrRate) - parseFloat(a.intrRate); // 기본 금리 내림차순
            }
            if (sortOption === "maxRate") {
                return parseFloat(b.intrRate2) - parseFloat(a.intrRate2); // 최고 금리 내림차순
            }
            return 0; // 기본 정렬
        });
    };

    return (
        <div>
            <div className="comparison-deposit">
                {/* 타입 선택 버튼 */}
                <div className="button-group">
                    <button
                        className={type === ProductType.SAVINGS ? "active" : ""}
                        onClick={() => handleTypeChange(ProductType.SAVINGS)}
                    >
                        적금 비교
                    </button>
                    <button
                        className={type === ProductType.FIXED ? "active" : ""}
                        onClick={() => handleTypeChange(ProductType.FIXED)}
                    >
                        예금 비교
                    </button>
                </div>

                {/* 필터 셀렉트 박스 */}
                <div className="filter-section">
                    <div className="filter-box">
                        <label htmlFor="sortSelect">정렬 기준: </label>
                        <select
                            id="sortSelect"
                            value={sortOption}
                            onChange={(e) => setSortOption(e.target.value)}
                        >
                            <option value="">선택</option>
                            <option value="bankName">은행명</option>
                            <option value="basicRate">기본 금리</option>
                            <option value="maxRate">최고 금리</option>
                        </select>
                    </div>
                </div>

                <div className="product-list">
                    {sortedData().length > 0 ? (
                        sortedData().map((product) => (
                            <div className="product-card" key={product.finPrdtCd}>
                                <h3>{product.finPrdtNm}</h3>
                                <hr />
                                <div className="text-matching">
                                    <p>
                                        <strong>은행명:</strong> <br /> {product.korCoNm}
                                    </p>
                                    <p>
                                        <strong>금리:</strong> <br /> {product.mtrtInt}
                                    </p>
                                    <p>
                                        <strong>우대조건:</strong> <br /> {product.spclCnd || "없음"}
                                    </p>
                                    <p>
                                        <strong>기본 금리:</strong> <br /> {product.intrRate}%
                                    </p>
                                    <p>
                                        <strong>최고 금리:</strong> <br /> {product.intrRate2}%
                                    </p>
                                </div>
                            </div>
                        ))
                    ) : (
                        <p>데이터가 없습니다.</p>
                    )}
                </div>
                <PageComponent serverData={serverData} movePage={moveToList} />
            </div>
        </div>
    );
};

export default ComparisonDepositComponent;