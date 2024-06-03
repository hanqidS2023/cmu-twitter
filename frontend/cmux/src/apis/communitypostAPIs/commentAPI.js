import { useFetchWithTokenRefresh } from "../../utils/ApiUtilsDynamic";


const API_URL = process.env.REACT_APP_POST_URL + "comments" || "";

export const useCommentApi = () => {
    const fetchWithTokenRefresh = useFetchWithTokenRefresh();

    const saveComment = async (commentData) => {
        try {
            const response = await fetchWithTokenRefresh(`${API_URL}`, {
                method: 'POST',
                body: JSON.stringify(commentData),
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const data = await response.json(); 
                return data;
              } else {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
            } catch (error) {
              console.error("Error getting posts:", error.message);
            }
    };

    const getComment = async (commentId) => {
        try {
            const response = await fetchWithTokenRefresh(`${API_URL}/${commentId}`, {
                method: 'GET'
            });
            if (response.ok) {
                const data = await response.json(); 
                return data;
              } else {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
            } catch (error) {
              console.error("Error getting posts:", error.message);
            }
    };

    const deleteComment = async (commentId) => {
        try {
            const response = await fetchWithTokenRefresh(`${API_URL}/${commentId}`, {
                method: 'DELETE'
            });
            if (response.ok) {
                const data = await response.json(); 
                return data;
              } else {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
            } catch (error) {
              console.error("Error getting posts:", error.message);
            }
    };

    const updateComment = async (commentId, commentData) => {
        try {
            const response = await fetchWithTokenRefresh(`${API_URL}/${commentId}`, {
                method: 'PUT',
                body: JSON.stringify(commentData),
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            if (response.ok) {
                const data = await response.json(); 
                return data;
              } else {
                throw new Error(`HTTP error! status: ${response.status}`);
              }
            } catch (error) {
              console.error("Error getting posts:", error.message);
            }
    };

    return { saveComment, getComment, deleteComment, updateComment };
}
