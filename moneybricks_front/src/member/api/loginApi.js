import axios from "axios";

export const API_SERVER_HOST = "http://localhost:8080";
const host = `${API_SERVER_HOST}/api/auth`;

export const loginPost = async (loginParam) => {
	const header = { headers: { "Content-Type": "x-www-form-urlencoded" } };

	const form = new FormData();
	form.append("username", loginParam.username);
	form.append("password", loginParam.password);

	const res = await axios.post(`${host}/login`, form, header);

	return res.data;
};
