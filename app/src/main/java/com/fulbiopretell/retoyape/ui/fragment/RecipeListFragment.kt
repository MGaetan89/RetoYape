package com.fulbiopretell.retoyape.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fulbiopretell.retoyape.R
import com.fulbiopretell.retoyape.adapter.RecipesAdapter
import com.fulbiopretell.retoyape.base.viewBinding
import com.fulbiopretell.retoyape.core.extensions.gone
import com.fulbiopretell.retoyape.core.extensions.hideKeyboard
import com.fulbiopretell.retoyape.core.extensions.show
import com.fulbiopretell.retoyape.databinding.FragmentRecipeListBinding
import com.fulbiopretell.retoyape.model.Recipe
import com.fulbiopretell.retoyape.viewmodel.RecipeListViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val DATA_RECIPE = "dataRecipe"

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.fragment_recipe_list) {

    private val binding by viewBinding(FragmentRecipeListBinding::bind)
    private var listRecipes = arrayListOf<Recipe>()

    private val viewmodel: RecipeListViewModel by viewModels()

    private val adapter: RecipesAdapter by lazy {
        RecipesAdapter { recipe ->
            val bundle = bundleOf(DATA_RECIPE to Gson().toJson(recipe))
            NavHostFragment.findNavController(this@RecipeListFragment)
                .navigate(R.id.action_recipeListFragment_to_recipeDetailFragment, bundle)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        loadData()
        initListener()
    }

    private fun initViews() {
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
    }

    private fun loadData() {
        viewmodel.getRecipes().observe(viewLifecycleOwner, {
            when (it) {
                is RecipeListViewModel.ViewState.Loading -> {
                    hideKeyboard(requireActivity())
                    binding.progressBar.show()
                    binding.list.gone()

                }

                is RecipeListViewModel.ViewState.ListRecipesLoaded -> {
                    listRecipes = it.listRecipes as ArrayList<Recipe>
                    adapter.submitList(it.listRecipes)
                    binding.progressBar.gone()
                    binding.list.show()
                }

                is RecipeListViewModel.ViewState.ListRecipesFailure -> {
                    Timber.e(it.error)
                }

            }
        })
    }

    private fun initListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) {
                    adapter.submitList(listRecipes)
                } else {
                    val listRecipesSearch = arrayListOf<Recipe>()
                    listRecipes.forEach {
                        if(it.name?.contains(newText.toString()) ?: false || it.description?.contains(newText.toString()) ?: false){
                            listRecipesSearch.add(it)
                        }
                    }

                    if(listRecipesSearch.size == 0) {
                        binding.tvWithOutResult.show()
                        binding.list.gone()
                        binding.progressBar.gone()
                    }else{
                        adapter.submitList(listRecipesSearch)
                        binding.tvWithOutResult.gone()
                        binding.list.show()
                        binding.progressBar.gone()
                    }
                }
                return false
            }
        })
    }
}

