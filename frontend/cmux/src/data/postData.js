// import { usePostApi } from '../apis/communitypostAPIs/postAPI';

// const { fetchPostsByAuthorIds, createPost, addLike, getPostById, deletePost, updatePost, markAsFindTeammatePost, addTeamMembers, searchPosts } = usePostApi();


// export const fetchPostsByIds = async (postIds) => {
//   const posts = await Promise.all(
//     postIds.map(async element => {
//       try {
//         // Attempt to fetch each post
//         return await getPostById(element);
//       } catch (error) {
//         console.error(`Error fetching post ${element}:`, error);
//         return null; // Return null or appropriate value if an error occurs
//       }
//     })
//   );

//   // Filter out null values (failed fetches)
//   const validPosts = posts.filter(post => post !== null);
//   console.log("type of validPosts: ", typeof validPosts);
//   return validPosts;
// };
