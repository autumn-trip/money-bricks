import useCustomMove from "../../common/hooks/useCustomMove";
import {useEffect, useState} from "react";
import {getList} from "../api/comparisonDepositApi";
import "../style/ComparisonDepositComponent.scss";
import PageComponent from "../../common/component/PageComponent";

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

const handleError = (err) => {
    console.error("Error occurred:", err);
    alert("데이터를 불러오는 중 오류가 발생했습니다.");
};

const ComparisonDepositComponent = () => {
    const { page, size , moveToList } = useCustomMove();
    const [serverData, setServerData] = useState(initState);
    const [type, setType] = useState("SAVING"); // 초기값: 적금

    useEffect(() => {
        getList({ page, size })
            .then((data) => {
                console.log("Fetched Data:", data);
                setServerData(data);
            })
            .catch((err) => handleError(err));
    }, [page, size]);

    const handleTypeChange = (newType) => {
        setType(newType);
    };

    return (
        <div className="comparison-deposit">
            {/* 타입 선택 버튼 */}
            <div className="button-group">
                <button
                    className={type === "SAVING" ? "active" : ""}
                    onClick={() => handleTypeChange("SAVING")}
                >
                    적금 비교
                </button>
                <button
                    className={type === "FIXED" ? "active" : ""}
                    onClick={() => handleTypeChange("FIXED")}
                >
                    예금 비교
                </button>
            </div>

            {/* 데이터 리스트 */}
            <div className="product-list">
                {serverData.dtoList.length > 0 ? (
                    serverData.dtoList.map((product) => (
                        <div className="product-card" key={product.finPrdtCd}>
                            <h3>{product.finPrdtNm}</h3>
                            <p>
                                <strong>은행명:</strong> {product.korCoNm}
                            </p>
                            <p>
                                <strong>금리:</strong> {product.mtrtInt}
                            </p>
                            <p>
                                <strong>우대조건:</strong> {product.spclCnd || "없음"}
                            </p>
                        </div>
                    ))
                ) : (
                    <p>데이터가 없습니다.</p>
                )}
            </div>
            <PageComponent serverData={serverData} movePage={moveToList} />
        </div>
    );
};

export default ComparisonDepositComponent;