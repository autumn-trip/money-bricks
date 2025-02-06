import { useState, useEffect } from "react";
import {getPointsInfo} from "../../member/api/pointApi";
import {deposit} from "../../member/api/accountApi";

const MAX_DEPOSIT = 10000; // 한 번에 최대 10,000 포인트까지만 입금 가능

const DepositComponent = () => {
	const [availablePoints, setAvailablePoints] = useState(0); // 사용 가능한 포인트
	const [depositAmount, setDepositAmount] = useState(""); // 입금 금액
	const [lastDepositDate, setLastDepositDate] = useState(null); // 마지막 입금 날짜
	const [isLoading, setIsLoading] = useState(true);

	// 🚀 **포인트 정보 가져오기**
	const fetchPointsInfo = async () => {
		try {
			const data = await getPointsInfo();
			setAvailablePoints(data.availablePoints);
			setLastDepositDate(localStorage.getItem("lastDepositDate")); // 로컬스토리지에서 마지막 입금 날짜 불러오기
		} catch (error) {
			console.error("포인트 정보 불러오기 실패:", error);
		} finally {
			setIsLoading(false);
		}
	};

	useEffect(() => {
		fetchPointsInfo();
	}, []);

	// 입금 요청
	const handleDeposit = async () => {
		const today = new Date().toISOString().split("T")[0]; // 오늘 날짜 (YYYY-MM-DD)

		// 하루 1회 입금 제한 체크
		if (lastDepositDate === today) {
			alert("오늘은 이미 입금하셨습니다. 내일 다시 시도해주세요.");
			return;
		}

		// 입금 금액 검증
		const amount = parseInt(depositAmount);
		if (isNaN(amount) || amount <= 0) {
			alert("올바른 입금 금액을 입력하세요.");
			return;
		}
		if (amount > availablePoints) {
			alert("사용 가능한 포인트를 초과하여 입금할 수 없습니다.");
			return;
		}
		if (amount > MAX_DEPOSIT) {
			alert(`한 번에 최대 ${MAX_DEPOSIT} 포인트까지만 입금할 수 있습니다.`);
			return;
		}

		try {
			// 입금 API 호출
			await deposit({ depositAmount: amount });

			// 포인트 정보 갱신
			setAvailablePoints(prev => prev - amount);
			setDepositAmount("");
			localStorage.setItem("lastDepositDate", today); // 오늘 입금했음을 저장
			setLastDepositDate(today);

			alert("입금이 완료되었습니다!");
		} catch (error) {
			alert(error.response?.data?.message || "입금 중 오류가 발생했습니다.");
		}
	};

	return (
		<div className="deposit-container">
			<h2>포인트 입금</h2>
			{isLoading ? (
				<p>로딩 중...</p>
			) : (
				<>
					<p>사용 가능한 포인트: {availablePoints} P</p>
					<input
						type="number"
						value={depositAmount}
						onChange={(e) => setDepositAmount(e.target.value)}
						placeholder="입금할 포인트 입력"
						min="1"
						max={MAX_DEPOSIT}
					/>
					<button onClick={handleDeposit} disabled={lastDepositDate === new Date().toISOString().split("T")[0]}>
						입금하기
					</button>
				</>
			)}
		</div>
	);
};

export default DepositComponent;
