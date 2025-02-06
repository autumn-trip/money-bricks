import React, { useEffect, useState } from "react";
import LoadingSpinner from "../../common/component/LoadingSpinner";
import {getSavingsAccount} from "../../member/api/accountApi";

const initState = {
	id: null,
	memberId: null,
	accountNumber: "",
	interestRate: 0,
	startDate: "",
	endDate: "",
	totalAmount: 0,
	status: "",
	depositCount: 0,
};

// 계좌 상태 변환 함수
const getStatusText = (status) => {
	switch (status) {
		case "ACTIVE":
			return "사용 중";
		case "COMPLETED":
			return "만기";
		case "CANCELED":
			return "중도 해지";
		default:
			return "알 수 없음";
	}
};

const AccountDetailsComponent = () => {
	const [account, setAccount] = useState(initState);
	const [isLoading, setIsLoading] = useState(true);
	const [error, setError] = useState("");

	useEffect(() => {
		const fetchAccount = async () => {
			try {
				const data = await getSavingsAccount();
				setAccount(data);
			} catch (error) {
				setError("계좌 정보를 불러오는 데 실패했습니다.");
			} finally {
				setIsLoading(false);
			}
		};

		fetchAccount();
	}, []);

	if (isLoading) {
		return <LoadingSpinner isLoading={true} />;
	}

	if (error) {
		return <div className="error-message">{error}</div>;
	}

	return (
		<div className="account-details-container">
			<h2>계좌 정보</h2>
			{account && (
				<div className="account-details">
					<p><strong>계좌 번호:</strong> {account.accountNumber}</p>
					<p><strong>이자율:</strong> {account.interestRate}%</p>
					<p><strong>시작 날짜:</strong> {account.startDate}</p>
					<p><strong>만기 날짜:</strong> {account.endDate}</p>
					<p><strong>총 입금된 포인트:</strong> {account.totalAmount}</p>
					<p><strong>계좌 상태:</strong> {getStatusText(account.status)}</p>
					<p><strong>입금 횟수:</strong> {account.depositCount} 회</p>

					{/* "사용 중(ACTIVE)"이면 '중도 해지' 버튼 */}
					{account.status === "ACTIVE" && (
						<button className="terminate-button">중도 해지</button>
					)}

					{/* "사용 중"이 아닌 경우 갱신 버튼 표시 */}
					{account.status !== "ACTIVE" && (
						<button className="renew-button">계좌 갱신하기</button>
					)}
				</div>
			)}
		</div>
	);
};

export default AccountDetailsComponent;
